package com.eos.admin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.entity.Notification;
import com.eos.admin.repository.NotificationRepository;
import com.eos.admin.service.NotificationService;

@Service
public class NotificationServiceImple implements NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Override
	public Notification notifyAdminNewEmployee(Long employeeId, String name) {
		
        String notificationMessage =  name + " has been Register form.";  
		Notification notification = new Notification(employeeId,notificationMessage, false);
       try {
		notificationRepository.save(notification);
       } catch (Exception e) {
           e.printStackTrace();
       }
	return notification;
	}

	@Override
	public List<Notification> getAllNotifications() {
		return notificationRepository.findAll();
		
	}

}
