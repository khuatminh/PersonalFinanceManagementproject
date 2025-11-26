package com.finance.service;
import com.finance.domain.Goal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.domain.Notification;
import com.finance.domain.User;
import com.finance.domain.Goal.GoalStatus;
import com.finance.repository.GoalRepository;
import com.finance.service.NotificationService;

@Service
@Transactional
public class GoalService {
    private final GoalRepository goalRepository ;
    private final NotificationService notificationService;

    public GoalService(GoalRepository goalRepository , NotificationService notificationService){
        this.goalRepository = goalRepository;
        this.notificationService = notificationService;
    }

    public List<Goal> findAll(){
        return goalRepository.findAll();
    }
    public Optional<Goal> findById(Long id){
        return goalRepository.findById(id);
    }
    public List<Goal> findByUser(User user){
        return goalRepository.findByUser(user);
    }    
    public List<Goal> findByUserOrderByTargetAsc(User user){
        return goalRepository.findByUserOrderByTargetDateAsc(user);
    }
   

    public List<Goal> findActiveGoalsByUser(User user) {
        return goalRepository.findActiveGoalsByUser(user, LocalDate.now());
    }

    public List<Goal> findCompletedGoalsByUser(User user) {
        return goalRepository.findCompletedGoalsByUser(user);
    }

    public List<Goal> findOverdueGoalsByUser(User user) {
        return goalRepository.findOverdueGoalsByUser(user, LocalDate.now());
    }

    // --- 2. CÁC HÀM THAY ĐỔI DỮ LIỆU (WRITE) ---

    public Goal createGoal(String name, BigDecimal targetAmount, LocalDate targetDate, User user, String description) {
        // Validation
        if (targetDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày hoàn thành không được ở quá khứ");
        }
        if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền mục tiêu phải lớn hơn 0");
        }

        Goal goal = new Goal(name, targetAmount, targetDate, user);
        goal.setDescription(description);
        goal.setStatus(GoalStatus.ACTIVE);
        goal.setCurrentAmount(BigDecimal.ZERO);

        Goal savedGoal = goalRepository.save(goal);

        // Gửi thông báo tạo mới
        notificationService.createNotification(user, "Mục tiêu mới '" + name + "' đã được tạo thành công!");

        return savedGoal;
    }

    public Goal updateGoal(Long id, String name, BigDecimal targetAmount, LocalDate targetDate, String description) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Goal với ID: " + id));

        // Validation cho Active Goal
        if (goal.getStatus() == GoalStatus.ACTIVE && targetDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Không thể cập nhật ngày quá khứ cho mục tiêu đang chạy");
        }
        if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền mục tiêu phải lớn hơn 0");
        }

        goal.setName(name);
        goal.setTargetAmount(targetAmount);
        goal.setTargetDate(targetDate);
        goal.setDescription(description);

        // Kiểm tra lại xem sau khi sửa tiền mục tiêu thì đã hoàn thành chưa
        if (goal.isCompleted()) {
            goal.markAsCompleted();
        }

        return goalRepository.save(goal);
    }

    public Goal contributeToGoal(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền đóng góp phải lớn hơn 0");
        }

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Goal với ID: " + id));

        if (goal.getStatus() != GoalStatus.ACTIVE) {
            throw new RuntimeException("Chỉ có thể đóng góp cho mục tiêu đang hoạt động");
        }

        // Cộng tiền
        goal.addProgress(amount);

        // Gửi thông báo tiến độ
        checkAndSendGoalNotifications(goal);
        
        // Kiểm tra hoàn thành
        if (goal.isCompleted()) {
            goal.markAsCompleted();
        }

        return goalRepository.save(goal);
    }

    public void deleteById(Long id) {
        if (!goalRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy Goal để xóa");
        }
        goalRepository.deleteById(id);
    }

    public void completeGoal(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " +id));
        goal.markAsCompleted();
        goalRepository.save(goal);
        notificationService.createNotification(goal.getUser(), "Bạn đã đánh dấu hoàn thành mục tiêu: " + goal.getName());
    }

    // --- 3. CÁC HÀM LOGIC NGHIỆP VỤ & THỐNG KÊ ---

    private void checkAndSendGoalNotifications(Goal goal) {
        double percentage = goal.getProgressPercentage();
        Integer lastNotified = goal.getLastNotificationPercentage();
        int currentMilestone = 0;

        // Xác định mốc hiện tại
        if (percentage >= 100) currentMilestone = 100;
        else if (percentage >= 75) currentMilestone = 75;
        else if (percentage >= 50) currentMilestone = 50;

        // Nếu đạt mốc mới cao hơn mốc cũ thì gửi thông báo
        if (currentMilestone > 0 && (lastNotified == null || currentMilestone > lastNotified)) {
            String msg;
            if (currentMilestone == 100) {
                msg = "Chúc mừng! Bạn đã hoàn thành mục tiêu '" + goal.getName() + "'!";
            } else {
                msg = "Cố lên! Bạn đã đạt " + currentMilestone + "% mục tiêu '" + goal.getName() + "'.";
            }
            
            notificationService.createNotification(goal.getUser(), msg);
            
            // Cập nhật mốc đã thông báo để không báo lại
            goal.setLastNotificationPercentage(currentMilestone);
        } else {
            // Thông báo đóng góp bình thường (không phải mốc quan trọng)
            // notificationService.createNotification(goal.getUser(), "Đã thêm tiền vào mục tiêu " + goal.getName());
        }
    }

    // Trả về DTO thống kê cho Controller
    public GoalSummary getGoalSummary(User user) {
        // 1. Lấy danh sách Active Goals để tính toán chi tiết
        List<Goal> activeGoals = goalRepository.findActiveGoalsByUser(user, LocalDate.now());
        
        // 2. Lấy số liệu thống kê khác từ DB
        long completedCount = goalRepository.countGoalsByUserAndStatus(user, GoalStatus.COMPLETED);
        long overdueCount = goalRepository.countOverdueGoalsByUser(user, LocalDate.now());

        // 3. Tính toán thủ công các chỉ số cộng dồn
        BigDecimal totalTargetAmount = BigDecimal.ZERO;
        BigDecimal totalCurrentAmount = BigDecimal.ZERO;
        long nearCompletionCount = 0; // Đếm mục tiêu sắp hoàn thành (>80%)

        for (Goal goal : activeGoals) {
            totalTargetAmount = totalTargetAmount.add(goal.getTargetAmount());
            totalCurrentAmount = totalCurrentAmount.add(goal.getCurrentAmount());

            if (goal.getProgressPercentage() >= 80.0) {
                nearCompletionCount++;
            }
        }

        // 4. Tính % tổng thể (Tránh lỗi chia cho 0)
        double overallProgressPercentage = 0.0;
        if (totalTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
            overallProgressPercentage = totalCurrentAmount.doubleValue() / totalTargetAmount.doubleValue() * 100.0;
        }

        // 5. Trả về đúng 7 tham số
        return new GoalSummary(
            activeGoals.size(),        // activeGoalsCount
            totalTargetAmount,         // totalTargetAmount
            totalCurrentAmount,        // totalCurrentAmount
            overallProgressPercentage, // overallProgressPercentage
            nearCompletionCount,       // nearCompletionCount
            overdueCount,              // overdueCount
            completedCount             // completedCount
        );
    }

    public static class GoalSummary {
        private final long activeGoalsCount;
        private final BigDecimal totalTargetAmount;
        private final BigDecimal totalCurrentAmount;
        private final double overallProgressPercentage;
        private final long nearCompletionCount;
        private final long overdueCount;
        private final long completedCount;

        public GoalSummary(long activeGoalsCount, BigDecimal totalTargetAmount, BigDecimal totalCurrentAmount,
                           double overallProgressPercentage, long nearCompletionCount, long overdueCount,
                           long completedCount) {
            this.activeGoalsCount = activeGoalsCount;
            this.totalTargetAmount = totalTargetAmount;
            this.totalCurrentAmount = totalCurrentAmount;
            this.overallProgressPercentage = overallProgressPercentage;
            this.nearCompletionCount = nearCompletionCount;
            this.overdueCount = overdueCount;
            this.completedCount = completedCount;
        }

        // --- BẮT BUỘC PHẢI CÓ GETTERS ---
        public long getActiveGoalsCount() { return activeGoalsCount; }
        public BigDecimal getTotalTargetAmount() { return totalTargetAmount; }
        public BigDecimal getTotalCurrentAmount() { return totalCurrentAmount; }
        public double getOverallProgressPercentage() { return overallProgressPercentage; }
        public long getNearCompletionCount() { return nearCompletionCount; }
        public long getOverdueCount() { return overdueCount; }
        public long getCompletedCount() { return completedCount; }
        
        // Hàm tiện ích tính số tiền còn thiếu
        public BigDecimal getTotalRemainingAmount() {
            return totalTargetAmount.subtract(totalCurrentAmount);
        }
    }
}
