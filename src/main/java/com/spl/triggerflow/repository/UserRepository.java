package com.spl.triggerflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spl.triggerflow.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}