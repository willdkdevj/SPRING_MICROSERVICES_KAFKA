package br.com.supernova.ecommercemicroservice.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCheckoutEnum {
    CREATED("Created"),
    APPROVED("Approved");

    private final String status;
}
