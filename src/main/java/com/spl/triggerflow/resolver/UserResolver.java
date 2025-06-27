package com.spl.triggerflow.resolver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spl.triggerflow.dto.user.CreateUserInput;
import com.spl.triggerflow.dto.user.UpdateUserInput;
import com.spl.triggerflow.entity.UserEntity;
import com.spl.triggerflow.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserResolver {

    private final UserService userService;

    public UserResolver(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public List<UserEntity> users() {
        return userService.findAllUsers();
    }

    @QueryMapping
    public UserEntity user(@Argument Long id) {
        return userService.findUserById(id).orElse(null);
    }

    @QueryMapping   
    public UserEntity getBasicDataUser(Authentication authentication) {
        return userService.getBasicDataUser(authentication);
    }

    @MutationMapping
    public UserEntity createUser(@Argument @Valid CreateUserInput input) {
        UserEntity user = new UserEntity();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setFatherName(input.getFatherName());
        user.setMotherName(input.getMotherName());
        user.setCountry(input.getCountry());
        user.setGender(input.getGender());
        if (input.getBirthday() != null && !input.getBirthday().isEmpty()) {
            LocalDate birthDate = LocalDate.parse(input.getBirthday(), DateTimeFormatter.ISO_LOCAL_DATE);
            user.setBirthday(birthDate);
        }
        user.setCellphone(input.getCellphone());
        user.setAge(input.getAge());
        user.setTimezone(input.getTimezone());
        user.setPassword(input.getPassword());

        return userService.createUser(user);
    }

    @MutationMapping
    public UserEntity updateUser(@Argument @Valid UpdateUserInput input) {
        return userService.updateUser(input);
    }
}
