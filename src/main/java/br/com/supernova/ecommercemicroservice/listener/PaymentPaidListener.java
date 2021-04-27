package br.com.supernova.ecommercemicroservice.listener;

import br.com.supernova.ecommercemicroservice.avro.payment.PaymentEventSink;
import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import br.com.supernova.ecommercemicroservice.service.CheckoutService;
import br.com.supernova.ecommercemicroservice.stream.Sink;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPaidListener {

    private final CheckoutService service;

    @StreamListener(Sink.INPUT)
    public void handler(PaymentEventSink eventSink){
        service.updateStatus(eventSink.getCheckoutCode().toString(), StatusCheckoutEnum.APPROVED);
    }
}
