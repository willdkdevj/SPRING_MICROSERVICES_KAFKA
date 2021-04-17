package br.com.supernova.ecommercemicroservice.resource.checkout;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/v1/checkout")
@RequiredArgsConstructor
public class CheckoutResource {

    private final CheckoutService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CheckoutResponse> create(@RequestBody CheckoutRequest request){
        log.warn("Checkout service still starting up (unable to parse response for checkout)");
        final CheckoutEntity entity = service.create(request).orElseThrow();

        log.info("Persistence validated and Response generated for the requester");
        final CheckoutResponse response = CheckoutResponse.builder()
                .code(entity.getCode())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
