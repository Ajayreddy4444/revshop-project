package com.example.demo.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.service.AddressClientService;

@Service
public class AddressClientServiceImpl implements AddressClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public AddressClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<AddressResponse> getAddressesByUser(Long userId) {

        String url = baseUrl + "/address/user/" + userId;

        AddressResponse[] response =
                restTemplate.getForObject(url, AddressResponse[].class);

        return Arrays.asList(response);
    }

    @Override
    public void saveAddress(AddressRequest request) {

        String url = baseUrl + "/address";

        restTemplate.postForObject(url, request, Object.class);
    }

    // ✅ NEW METHOD
    @Override
    public AddressResponse getAddressById(Long id) {

        String url = baseUrl + "/address/" + id;

        return restTemplate.getForObject(url, AddressResponse.class);
    }

    // ✅ NEW METHOD
    @Override
    public void updateAddress(AddressRequest request) {

        String url = baseUrl + "/address/update";

        restTemplate.put(url, request);
    }
}