package com.esporte.myapp.controller;

import com.esporte.myapp.dto.AddressRequest;
import com.esporte.myapp.dto.AddressResponse;
import com.esporte.myapp.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<AddressResponse>> getAddress(@PathVariable String userId){
        return ResponseEntity.status(HttpStatus.OK).body(service.get(userId));
    }
    @PatchMapping("/setDefault/{addressId}/{userId}")
    public ResponseEntity<List<AddressResponse>> setAddressAsDefault(@PathVariable UUID addressId,@PathVariable String userId){
        return ResponseEntity.status(HttpStatus.OK).body(service.setDefaultAddress(userId, addressId));

    }
    @PutMapping("/{userId}")
    public ResponseEntity<List<AddressResponse>> updateAddress( @PathVariable String userId, @RequestBody AddressRequest req){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(userId,req));
    }
    @DeleteMapping("/{addressId}/{userId}")
    public ResponseEntity<List<AddressResponse>> deleteAddress(@PathVariable UUID addressId, @PathVariable String userId){
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(userId, addressId));
    }
}
