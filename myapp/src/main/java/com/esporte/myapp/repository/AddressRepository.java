package com.esporte.myapp.repository;

import com.esporte.myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.esporte.myapp.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID>  {
    List<Address> findByUser_Id(String userId);
}
