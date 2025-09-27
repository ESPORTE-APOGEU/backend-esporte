package com.esporte.myapp.service;

import com.esporte.myapp.dto.AddressRequest;
import com.esporte.myapp.entity.Address;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.AddressRepository;
import com.esporte.myapp.repository.UserRepository;
import com.esporte.myapp.dto.AddressResponse;
import com.esporte.myapp.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repo;
    private final UserRepository userRepo;

    private  AddressResponse toResponse(Address a){
        return new AddressResponse(
                a.getAddressId(),
                a.getName(),
                a.getPostalCode(),
                a.getCity(),
                a.getState(),
                a.getDistrict(),
                a.getStreet(),
                a.getComplement(),
                a.getNumber(),
                a.getDefaultAddress()
        );
    }
    public List<AddressResponse> create(String userId, AddressRequest reqAddress){
        List<Address> allAddress =  repo.findByUser_Id(userId);
        if (allAddress.size() > 2){ //Aqui Limite maximo de endereço por pessoa
            throw new IllegalArgumentException("Voce não pode adicionar mais endereços.");
        }
        Optional<User> repoUser = userRepo.findById(userId);
        User user = repoUser.orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado") );

        Address newAddress = new Address(
                user,
                reqAddress.name(),
                reqAddress.postalCode(),
                reqAddress.city(),
                reqAddress.state(),
                reqAddress.district(),
                reqAddress.street(),
                reqAddress.number(),
                reqAddress.complement(),
                allAddress.isEmpty() // Arg Boleana Endereço padrão, se a lista está vazia o primeiro é padrão
        );
        repo.save(newAddress);
        allAddress.add(newAddress);
        return allAddress.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    };

    public List<AddressResponse> get(String userId){
        List<Address> addressesRepo =  repo.findByUser_Id(userId);
        return addressesRepo.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    };
    public List<AddressResponse> setDefaultAddress(String userId, UUID addressId){
        List<Address> addresses =  repo.findByUser_Id(userId);
        for(Address address : addresses){
            address.setDefaultAddress(address.getAddressId().equals(addressId));
        }
        repo.saveAll(addresses);
        return addresses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public List<AddressResponse> update(String userId,AddressRequest reqAddress){
        UUID addressId = reqAddress.addressId();
        if (addressId == null) {
            throw new IllegalArgumentException("O ID do endereço é obrigatório.");
        }
        Optional<Address> repoAddress = repo.findById(addressId);
        Address address = repoAddress.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado") );
        address.setName(reqAddress.name());
        address.setPostalCode(reqAddress.postalCode());
        address.setCity(reqAddress.city());
        address.setState(reqAddress.state());
        address.setDistrict(reqAddress.district());
        address.setStreet(reqAddress.street());
        address.setNumber(reqAddress.number());
        address.setComplement(reqAddress.complement());
        repo.save(address);

        List<Address> addressesRepo =  repo.findByUser_Id(userId);
        return addressesRepo.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    };
    public List<AddressResponse> delete(String userId, UUID address){
        List<Address> addressesRepo =  repo.findByUser_Id(userId);
        Optional<Address> addressOpt = addressesRepo.stream()
                .filter(a -> a.getAddressId().equals(address))
                .findFirst();
        Address addressObj =  addressOpt.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));
        repo.delete(addressObj);
        addressesRepo.remove(addressObj);
        return addressesRepo.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    };
}

