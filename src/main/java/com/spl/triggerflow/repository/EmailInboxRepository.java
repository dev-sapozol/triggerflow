package com.spl.triggerflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spl.triggerflow.entity.EmailInboxEntity;

public interface EmailInboxRepository extends JpaRepository<EmailInboxEntity, Long> {
}
