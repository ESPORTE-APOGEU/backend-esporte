package com.esporte.myapp.entity;
import io.jsonwebtoken.lang.Strings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter @Setter @NoArgsConstructor //@AllArgsConstructor
public class Address {
    @Id
    private UUID addressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String name;

    @Column
    private String postalCode; //CEP

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String district; // Bairro

    @Column
    private String street; //Rua

    @Column
    private String number;

    @Column
    private String complement;

    @Column
    private Boolean defaultAddress = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Address( User user,String name, String postalCode,String city, String state, String district, String street, String number, String complement, Boolean defaultAddress){
        this.addressId = UUID.randomUUID();
        this.user = user;
        this.name =name;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.district = district;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.defaultAddress =  defaultAddress != null && defaultAddress;
        this.createdAt = LocalDateTime.now();

    }
}
