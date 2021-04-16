package br.com.supernova.ecommercemicroservice.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCheckoutEnum {
    CREATED("Created"),
    CANCELED("Canceled"),
    APPROVED("Approved"),
    DISAPPROVED("Disapproved");

    private final String status;
}
