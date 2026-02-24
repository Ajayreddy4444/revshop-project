package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Address;
import com.example.demo.repository.AddressRepository;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // 🔹 Save Address
    @PostMapping
    public Address saveAddress(@RequestBody Address address) {
        return addressRepository.save(address);
    }

    // 🔹 Get Addresses By User
    @GetMapping("/user/{userId}")
    public List<Address> getAddressesByUser(@PathVariable Long userId) {
        return addressRepository.findByUserId(userId);
    }
}

