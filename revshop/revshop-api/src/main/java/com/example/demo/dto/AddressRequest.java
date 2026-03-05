package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddressRequest {

    private Long userId;
    private Long addressId;

        @NotBlank(message = "Full name is required")
        @Pattern(regexp = "^[A-Za-z ]{3,50}$",
                message = "Name must contain only letters")
        private String fullName;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[6-9]\\d{9}$",
                message = "Enter valid 10 digit mobile number")
        private String phoneNumber;

        @NotBlank(message = "Address is required")
        @Pattern(regexp = "^[A-Za-z0-9, ]{5,100}$",
                message = "Address must be 5-100 characters")
        private String addressLine;

        @NotBlank(message = "City is required")
        @Pattern(regexp = "^[A-Za-z ]{2,50}$",
                message = "City must contain only letters")
        private String city;

        @NotBlank(message = "State is required")
        @Pattern(regexp = "^[A-Za-z ]{2,50}$",
                message = "State must contain only letters")
        private String state;

        @NotBlank(message = "Pincode is required")
        @Pattern(regexp = "^[1-9][0-9]{5}$",
                message = "Enter valid 6 digit pincode")
        private String pincode;

        // getters and setters
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAddressLine() {
		return addressLine;
	}
	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

    
  
}