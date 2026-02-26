package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;

public interface AddressClientService {

    List<AddressResponse> getAddressesByUser(Long userId);
    void saveAddress(AddressRequest request); 
    AddressResponse getAddressById(Long id);

    void updateAddress(AddressRequest request);
}