package com.security.book.controller.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class SignupDto {

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> roles;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}
