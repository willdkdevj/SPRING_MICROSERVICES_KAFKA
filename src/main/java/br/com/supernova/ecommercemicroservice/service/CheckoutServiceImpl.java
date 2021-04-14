package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.entity.CheckoutItemEntity;
import br.com.supernova.ecommercemicroservice.entity.ShippingEntity;
import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import br.com.supernova.ecommercemicroservice.repository.CheckoutRepository;
import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{

    private final CheckoutRepository repository;

    @Override
    public Optional<CheckoutEntity> create(CheckoutRequest checkoutRequest) {
        CheckoutEntity createdEntity = createEntityFromRequest(checkoutRequest);
        return Optional.of(repository.save(createdEntity));
    }

    @Override
    public Optional<CheckoutEntity> updateStatus(String checkoutCode, StatusCheckoutEnum status) {
        return Optional.empty();
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
