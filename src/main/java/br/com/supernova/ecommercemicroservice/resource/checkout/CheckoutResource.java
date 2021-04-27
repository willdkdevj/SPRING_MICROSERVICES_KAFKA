package br.com.supernova.ecommercemicroservice.resource.checkout;

import br.com.supernova.ecommercemicroservice.avro.checkout.CheckoutEventSource;
import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.service.CheckoutService;
import br.com.supernova.ecommercemicroservice.stream.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Slf4j
@RequestMapping("/v1/checkout")
@RequiredArgsConstructor
public class CheckoutResource {

    private final CheckoutService service;
    private final Source source;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CheckoutResponse> create(@RequestBody CheckoutRequest request){
        log.warn("Checkout service still starting up (unable to parse response for checkout)");
        final CheckoutEntity entity = service.create(request).orElseThrow();

        log.info("Preparing wrapper for sending message");
        final CheckoutEventSource checkoutEventSource = CheckoutEventSource.newBuilder()
                .setCheckoutCode(entity.getCode())
                .setStatus(entity.getStatus().name())
                .build();
        source.output().send(MessageBuilder.withPayload(checkoutEventSource).build());

        log.info("Persistence validated and Response generated for the requester");
        final CheckoutResponse response = CheckoutResponse.builder()
                .code(entity.getCode())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
