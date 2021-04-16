package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.entity.CheckoutItemEntity;
import br.com.supernova.ecommercemicroservice.entity.ShippingEntity;
import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import br.com.supernova.ecommercemicroservice.repository.CheckoutRepository;
import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutRequest;
import br.com.supernova.ecommercemicroservice.streaming.CheckoutEventStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{

    private final CheckoutRepository repository;

    @Override
    public Optional<CheckoutEntity> create(CheckoutRequest checkoutRequest) {
        log.info("Treatment of the Request initiated to persist to the Database");
        CheckoutEntity createdEntity = createEntityFromRequest(checkoutRequest);

        log.info("Creating Event related to the status: {}", createdEntity.getStatus().name());
        CheckoutEventStream eventStream = CheckoutEventStream.builder()
                .checkoutCode(createdEntity.getCode())
                .status(createdEntity.getStatus())
                .build();

        log.info("Sending checkout event: {}", eventStream);
        MessageBuilder.withPayload(eventStream).setHeader("messageKey", createdEntity.getCode());

        log.info("Persisting entity in the database");
        return Optional.of(repository.save(createdEntity));
    }

    @Override
    public Optional<CheckoutEntity> updateStatus(String checkoutCode, StatusCheckoutEnum status) {
        repository.findByEntity(checkoutCode);
        return Optional.empty();
    }

    @Override
    public Optional<CheckoutEntity> fetchByEntityCode(String code) {
        return repository.findByEntity(code);
    }

    @Override
    public Optional<CheckoutEntity> fetchByEntityID(Long id) {
        return repository.findById(id);
    }

    private CheckoutEntity createEntityFromRequest(CheckoutRequest checkoutRequest){
        final ShippingEntity shipping = ShippingEntity.builder()
                .address(checkoutRequest.getAddress())
                .complement(checkoutRequest.getComplement())
                .country(checkoutRequest.getCountry())
                .state(checkoutRequest.getState())
                .cep(checkoutRequest.getCep())
                .build();

        final CheckoutEntity entity = CheckoutEntity.builder()
                .code(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .status(StatusCheckoutEnum.CREATED)
                .saveAddress(checkoutRequest.getSaveAddress())
                .saveInformation(checkoutRequest.getSaveInfo())
                .build();

        List<CheckoutItemEntity> newList = new ArrayList<>();
        for (String product : checkoutRequest.getProducts()) {
            CheckoutItemEntity itemEntity = CheckoutItemEntity.builder()
                    .product(product)
                    .checkout(entity)
                    .build();
        newList.add(itemEntity);
        }

        shipping.setCheckout(entity);
        entity.setItems(newList);
        entity.setShipping(shipping);

        return entity;
    }
}
