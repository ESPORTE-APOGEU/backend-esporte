package com.esporte.myapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

public record AddressResponse(
        @Nullable @JsonProperty("id") UUID addressId,
        @JsonProperty("Nome") String nome,
        @JsonProperty("CEP") String postalCode,
        @JsonProperty("Cidade") String city,
        @JsonProperty("UF") String state,
        @JsonProperty("Bairro") String district,
        @JsonProperty("Rua") String street,
        @JsonProperty("Numero") String number,
        @JsonProperty("Complemento") String complement,
        @JsonProperty("padrao") Boolean defaultAddress
) {}
