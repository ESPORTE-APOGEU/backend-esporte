package com.esporte.myapp.controller;

import com.esporte.myapp.dto.AddressRequest;
import com.esporte.myapp.dto.AddressResponse;
import com.esporte.myapp.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/users/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService service;
    private String getUserId(Jwt jwt, org.springframework.security.core.Authentication authentication) {
        String userId = (jwt != null) ? jwt.getSubject() : null;
        if (userId == null && authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String s) {
                userId = s;
            } else {
                userId = authentication.getName();
            }
        }
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }
        return userId;
    }

    @PostMapping
    public ResponseEntity<List<AddressResponse>> createAddress(
            @Valid @RequestBody AddressRequest req,
            @AuthenticationPrincipal Jwt jwt,
            org.springframework.security.core.Authentication authentication
            ){
        String userId = getUserId(jwt, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, req));
    }
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddress(
            @AuthenticationPrincipal Jwt jwt,
            org.springframework.security.core.Authentication authentication){
        String userId = getUserId(jwt,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(service.get(userId));
    }
    @PatchMapping("/set_default/{addressId}")
    public ResponseEntity<List<AddressResponse>> setAddressAsDefault(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal Jwt jwt,
            org.springframework.security.core.Authentication authentication
            ){
        String userId = getUserId(jwt, authentication);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.setDefaultAddress(userId, addressId));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(service.setDefaultAddress(userId, addressId));
        }
    }
    @PutMapping
    public ResponseEntity<List<AddressResponse>> updateAddress(
            @RequestBody AddressRequest req,
            @AuthenticationPrincipal Jwt jwt,
            org.springframework.security.core.Authentication authentication
            ){
        String userId = getUserId(jwt,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(service.update(userId,req));
    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<List<AddressResponse>> deleteAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal Jwt jwt,
            org.springframework.security.core.Authentication authentication
        ){
        String userId = getUserId(jwt,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(userId, addressId));
    }
}
