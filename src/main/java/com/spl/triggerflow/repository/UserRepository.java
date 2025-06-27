package com.spl.triggerflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spl.triggerflow.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}