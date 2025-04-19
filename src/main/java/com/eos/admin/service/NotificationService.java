package com.eos.admin.service;

import java.util.List;

import com.eos.admin.entity.Notification;

public interface NotificationService {
	public Notification notifyAdminNewEmployee(Long employeeId, String string);
	List<Notification> getAllNotifications();

}
