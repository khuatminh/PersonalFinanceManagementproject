
package com.finance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.domain.Notification;
import com.finance.domain.User;

public interface NotificationRepository  extends JpaRepository<Notification,Long>{
      List<Notification> findByUserOrderByCreatedAtDesc(User user);

      long countByUserAndIsReadFalse(User user);
}

    

