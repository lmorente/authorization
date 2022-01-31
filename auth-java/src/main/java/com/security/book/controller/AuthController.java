package com.security.book.controller;

import com.security.book.repository.entity.ERole;
import com.security.book.repository.entity.Role;
import com.security.book.repository.entity.User;
import com.security.book.controller.dto.JwtResponseDto;
import com.security.book.jwt.JwtUtils;
import com.security.book.repository.RoleRepository;
import com.security.book.repository.UserRepository;
import com.security.book.controller.dto.LoginDto;
import com.security.book.controller.dto.SignupDto;
import com.security.book.security.UserDetailsImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder encoder;

  private final JwtUtils jwtUtils;

  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                        RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {
    final Authentication authentication = this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    final UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
    final List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    return ResponseEntity.ok(new JwtResponseDto(this.jwtUtils.generateJwtToken(authentication),
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signUpRequest) {
    if (this.userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body("Error: Username is already taken!");
    }
    if (this.userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body("Error: Email is already in use!");
    }

    // Create new user's account
    final User user = new User(signUpRequest.getUsername(),
               signUpRequest.getEmail(),
               this.encoder.encode(signUpRequest.getPassword()));
    final Set<String> strRoles = signUpRequest.getRoles();
    final Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      final Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() ->
              new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        if ("ROLE_ADMIN".equals(role)) {
          final Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
        } else {
          final Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }
    user.setRoles(roles);
    this.userRepository.save(user);
    return ResponseEntity.ok("User registered successfully!");
  }
}
