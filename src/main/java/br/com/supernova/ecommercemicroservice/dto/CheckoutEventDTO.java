package br.com.supernova.ecommercemicroservice.dto;

import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutEventDTO {

    String code;
    StatusCheckoutEnum status;

}
