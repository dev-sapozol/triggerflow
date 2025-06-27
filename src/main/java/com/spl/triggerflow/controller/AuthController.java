package com.spl.triggerflow.controller;

import com.spl.triggerflow.dto.user.LoginRequest;
import com.spl.triggerflow.dto.user.RegisterRequest;
import com.spl.triggerflow.entity.UserEntity;
import com.spl.triggerflow.repository.UserRepository;
import com.spl.triggerflow.security.CustomUserDetails;
import com.spl.triggerflow.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request,
      HttpServletResponse response) {
    try {
      Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
      UserEntity user = principal.getUser(); // ← entidad real

      String token = jwtService.generateUserToken(user); // ← nuevo método

      int maxAge = user.isSuperAdmin() ? 31_536_000 : 86_400;

      Cookie cookie = new Cookie("auth_token", token);
      cookie.setHttpOnly(true);
      cookie.setSecure(true); // solo en HTTPS
      cookie.setPath("/");
      cookie.setMaxAge(maxAge);
      response.addCookie(cookie);

      return ResponseEntity.ok(Map.of("token", token));
    } catch (AuthenticationException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "invalid-credentials"));
    }
  }

  @GetMapping("/email-exists")
  public ResponseEntity<?> checkEmail(@RequestParam String email) {
    boolean exists = userRepository.findByEmail(email).isPresent();
    return ResponseEntity.ok().body(Map.of("exists", exists));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("auth_token", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    return ResponseEntity.ok(Map.of("message", "logout"));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
      HttpServletResponse response) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error", "email-already-exists"));
    }

    UserEntity user = new UserEntity();
    user.setName(request.getName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEmail(request.getEmail());
    user.setFatherName(request.getFatherName());
    user.setMotherName(request.getMotherName());
    user.setCountry(request.getCountry());
    user.setGender(request.getGender());

    if (request.getBirthday() != null && !request.getBirthday().isEmpty()) {
      LocalDate birth = LocalDate.parse(request.getBirthday(), DateTimeFormatter.ISO_LOCAL_DATE);
      user.setBirthday(birth);
      user.setAge(Period.between(birth, LocalDate.now()).getYears());
    }

    user.setTimezone(request.getTimezone());
    user.setRole(
        switch (request.getRole()) {
          case "ADMIN", "SUPERADMIN" -> request.getRole();
          default -> "USER";
        });
    user.setLanguage(request.getLanguage());

    userRepository.save(user);

    String token = jwtService.generateUserToken(user); // ← actualizado
    int maxAge = user.isSuperAdmin() ? 31_536_000 : 86_400;

    Cookie cookie = new Cookie("auth_token", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("token", token));
  }
}
