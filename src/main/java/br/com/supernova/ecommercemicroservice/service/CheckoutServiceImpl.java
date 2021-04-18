package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.dto.CheckoutEventDTO;
import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.entity.CheckoutItemEntity;
import br.com.supernova.ecommercemicroservice.entity.ShippingEntity;
import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import br.com.supernova.ecommercemicroservice.repository.CheckoutRepository;
import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{

    private final CheckoutRepository repository;
    private final CheckoutServiceStream serviceStream;

    @Override
    public Optional<CheckoutEntity> create(CheckoutRequest checkoutRequest) {
        log.info("Treatment of the Request initiated to persist to the Database");
        CheckoutEntity createdEntity = createEntityFromRequest(checkoutRequest);

        CheckoutEventDTO dataDTO = CheckoutEventDTO.builder()
                .code(createdEntity.getCode())
                .status(createdEntity.getStatus())
                .build();
        serviceStream.send(dataDTO);

        log.info("Persisting entity in the database");
        return Optional.of(repository.save(createdEntity));
    }

    @Override
    public Optional<CheckoutEntity> updateStatus(String checkoutCode, StatusCheckoutEnum status) {
        final CheckoutEntity entity = repository.findByCode(checkoutCode).orElse(CheckoutEntity.builder().build());
        entity.setStatus(StatusCheckoutEnum.APPROVED);
        return Optional.of(repository.save(entity));
    }


    private CheckoutEntity createEntityFromRequest(CheckoutRequest checkoutRequest){

        final CheckoutEntity entity = CheckoutEntity.builder()
                .code(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .status(StatusCheckoutEnum.CREATED)
                .saveAddress(checkoutRequest.getSaveAddress())
                .saveInformation(checkoutRequest.getSaveInfo())
                .shipping(ShippingEntity.builder()
                        .address(checkoutRequest.getAddress())
                        .complement(checkoutRequest.getComplement())
                        .country(checkoutRequest.getCountry())
                        .state(checkoutRequest.getState())
                        .cep(checkoutRequest.getCep())
                        .build())
                .build();

        entity.setItems(checkoutRequest.getProducts()
                .stream()
                .map(item -> CheckoutItemEntity.builder()
                    .checkout(entity).product(item)
                        .build())
                .collect(Collectors.toList()));

        return entity;
    }
}
