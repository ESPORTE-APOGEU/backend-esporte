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
    @PostMapping("/{userId}")
    public ResponseEntity<List<AddressResponse>> createAddress(@Valid @RequestBody AddressRequest req, @PathVariable String userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, req));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddress(@PathVariable String userId, @AuthenticationPrincipal Jwt jwt, org.springframework.security.core.Authentication authentication){
        String clerkId = (jwt != null) ? jwt.getSubject() : null;
        if (clerkId == null && authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String s) {
                clerkId = s;               // ex.: "user_abc"
            } else {
                clerkId = authentication.getName(); // fallback
            }
        }

        if (clerkId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autenticado");
        }
        System.out.print("Clerk ID obtido"+clerkId);
        return ResponseEntity.status(HttpStatus.OK).body(service.get(userId));
    }
    @PatchMapping("/set_default/{addressId}/{userId}")
    public ResponseEntity<List<AddressResponse>> setAddressAsDefault(@PathVariable UUID addressId,@PathVariable String userId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.setDefaultAddress(userId, addressId));
        } catch ( IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(service.setDefaultAddress(userId, addressId));
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<List<AddressResponse>> updateAddress( @PathVariable String userId, @RequestBody AddressRequest req){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(userId,req));
    }
    @DeleteMapping("/{addressIdStr}/{userId}")
    public ResponseEntity<List<AddressResponse>> deleteAddress(@PathVariable String addressIdStr, @PathVariable String userId){
        System.out.print("a");
        UUID addressId = UUID.fromString(addressIdStr);
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(userId, addressId));
    }
}
