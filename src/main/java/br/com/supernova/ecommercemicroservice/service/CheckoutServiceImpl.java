package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import br.com.supernova.ecommercemicroservice.repository.CheckoutRepository;
import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{

    private final CheckoutRepository repository;

    @Override
    public Optional<CheckoutEntity> create(CheckoutRequest checkoutRequest) {
        final CheckoutEntity entity = CheckoutEntity.builder()
                .code(UUID.randomUUID().toString())
                .build();
        return Optional.of(repository.save(entity));
    }

    @Override
    public Optional<CheckoutEntity> updateStatus(String checkoutCode, StatusCheckoutEnum status) {
        return Optional.empty();
    }
}
