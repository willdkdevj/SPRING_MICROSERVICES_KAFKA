package br.com.supernova.ecommercemicroservice.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String INPUT = "payment-paid-input";

    @Input(INPUT)
    SubscribableChannel input();
}
