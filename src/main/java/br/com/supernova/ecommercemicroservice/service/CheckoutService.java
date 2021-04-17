package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutRequest;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CheckoutService {

    Optional<CheckoutEntity> create(CheckoutRequest checkoutRequest);

    Optional<CheckoutEntity> updateStatus(String checkoutCode, StatusCheckoutEnum status);

}
