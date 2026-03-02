package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

	@NotBlank(message = "Please enter your full name")
    private String name;

    @NotBlank(message = "Please enter your email")
    private String email;

    @NotBlank(message = "Please enter your phone number")
    private String phone;

    @NotBlank(message = "Please enter your password")
    private String password;

    @NotBlank(message = "Please select a role")
    private String role;
    public RegisterRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}