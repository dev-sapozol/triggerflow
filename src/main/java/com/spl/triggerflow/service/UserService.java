package com.spl.triggerflow.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spl.triggerflow.dto.user.UpdateUserInput;
import com.spl.triggerflow.entity.UserEntity;
import com.spl.triggerflow.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity getBasicDataUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(user -> new UserEntity(
                        user.getId(),
                        user.getName(),
                        user.getFatherName(),
                        user.getMotherName(),
                        user.getEmail(),
                        user.getCountry(),
                        user.getCellphone(),
                        user.getTimezone(),
                        user.getRole(),
                        user.getLanguage()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity createUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public UserEntity updateUser(UpdateUserInput input) {
        UserEntity user = userRepository.findById(input.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (input.getName()        != null) user.setName(input.getName());
        if (input.getFatherName()  != null) user.setFatherName(input.getFatherName());
        if (input.getMotherName()  != null) user.setMotherName(input.getMotherName());
        if (input.getEmail()       != null) user.setEmail(input.getEmail());
        if (input.getCountry()     != null) user.setCountry(input.getCountry());
        if (input.getGender()      != null) user.setGender(input.getGender());
        if (input.getCellphone()   != null) user.setCellphone(input.getCellphone());
        if (input.getAge()         != null) user.setAge(input.getAge());
        if (input.getTimezone()    != null) user.setTimezone(input.getTimezone());

        if (input.getBirthday() != null && !input.getBirthday().isBlank()) {
            LocalDate birthDate = LocalDate.parse(input.getBirthday(), DateTimeFormatter.ISO_LOCAL_DATE);
            user.setBirthday(birthDate);
        }

        if (input.getPassword() != null && !input.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(input.getPassword()));
        }

        return userRepository.save(user);
    }
}
