package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

	@NotBlank(message = "Please enter your full name")
	@Size(min = 3, max = 50, message = "Name must be at least 3 characters")
	private String name;

	@NotBlank(message = "Please enter your email")
	@Email(message = "Enter a valid email address")
	private String email;

	@NotBlank(message = "Please enter your phone number")
	@Pattern(regexp="^[0-9]{10}$", message="Phone number must be 10 digits")
	private String phone;

	@NotBlank(message = "Please enter your password")
	@Size(min=6, message="Password must be at least 6 characters")
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