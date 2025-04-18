package com.eos.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eos.admin.entity.Notification;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
