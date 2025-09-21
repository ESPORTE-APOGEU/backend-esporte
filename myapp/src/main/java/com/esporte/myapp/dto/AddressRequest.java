package com.esporte.myapp.dto;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record AddressRequest(
        @Nullable @JsonProperty("id") UUID addressId,
        @JsonProperty("Nome") String name,
        @JsonProperty("CEP") String postalCode,
        @JsonProperty("Cidade") String city,
        @JsonProperty("UF") String state,
        @JsonProperty("Bairro") String district,
        @JsonProperty("Rua") String street,
        @JsonProperty("Numero") String number,
        @JsonProperty("Complemento") String complement
) {}
