package com.spl.triggerflow.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import com.spl.triggerflow.entity.UserEntity;

import java.util.Collections;

public class CustomUserDetails extends User {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        super(
                user.getEmail(), // username
                user.getPassword(), // hashed pwd
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
        );
        this.user = user;
    }

    public UserEntity getUser() {
        return user; // acceso a la entidad completa
    }
}