package com.example.demo.serviceImpl;

import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.service.AddressClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AddressClientServiceImpl implements AddressClientService {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${backend.base-url}")
    private String baseUrl;

    public AddressClientServiceImpl(RestTemplate restTemplate,
                                    HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    // 🔥 Attach JWT Token
    private HttpHeaders getHeaders() {

        String token = (String) request.getSession().getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null) {
            headers.setBearerAuth(token);
        }

        return headers;
    }

    @Override
    public List<AddressResponse> getAddressesByUser(Long userId) {

        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<AddressResponse[]> response =
                restTemplate.exchange(
                        baseUrl + "/address/user/" + userId,
                        HttpMethod.GET,
                        entity,
                        AddressResponse[].class
                );

        return Arrays.asList(response.getBody());
    }

    @Override
    public void saveAddress(AddressRequest requestBody) {

        HttpEntity<AddressRequest> entity =
                new HttpEntity<>(requestBody, getHeaders());

        restTemplate.exchange(
                baseUrl + "/address",
                HttpMethod.POST,
                entity,
                Void.class
        );
    }

    @Override
    public AddressResponse getAddressById(Long id) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        ResponseEntity<AddressResponse> response =
                restTemplate.exchange(
                        baseUrl + "/address/" + id,
                        HttpMethod.GET,
                        entity,
                        AddressResponse.class
                );

        return response.getBody();
    }

    @Override
    public void updateAddress(AddressRequest requestBody) {

        HttpEntity<AddressRequest> entity =
                new HttpEntity<>(requestBody, getHeaders());

        restTemplate.exchange(
                baseUrl + "/address/update",
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }
}