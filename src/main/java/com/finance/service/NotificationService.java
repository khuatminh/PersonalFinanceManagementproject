package com.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.repository.NotificationRepository;
import com.finance.domain.Notification;
import com.finance.domain.User;
import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User user, String message) {
      Notification notification = new Notification(user , message);

        notificationRepository.save(notification);
    }
    
    
    

}
