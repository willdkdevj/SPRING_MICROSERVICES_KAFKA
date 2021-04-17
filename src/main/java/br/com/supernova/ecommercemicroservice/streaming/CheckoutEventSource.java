package br.com.supernova.ecommercemicroservice.streaming;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CheckoutEventSource {

    String OUTPUT = "checkout-created-output";

    @Output(CheckoutEventSource.OUTPUT)
    MessageChannel output();
}
