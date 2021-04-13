package br.com.supernova.ecommercemicroservice.resource.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse implements Serializable {
    private String code;
}
