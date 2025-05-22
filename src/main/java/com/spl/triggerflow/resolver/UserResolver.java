package com.spl.triggerflow.resolver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.spl.triggerflow.dto.user.CreateUserInput;
import com.spl.triggerflow.entity.UserEntity;
import com.spl.triggerflow.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class UserResolver {
  
  private final UserRepository userRepository;

  public UserResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @QueryMapping
  public List<UserEntity> users() {
    return userRepository.findAll();
  }

  @QueryMapping
  public UserEntity user(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @MutationMapping
  public UserEntity createUser(@Argument @Valid CreateUserInput input) {
    UserEntity user = new UserEntity();
    user.setName(input.getName());
    user.setPassword(input.getPassword());
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
    return userRepository.save(user);
  }
}
