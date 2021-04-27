package br.com.supernova.ecommercemicroservice.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {
    String OUTPUT = "checkout-created-output";

    @Output(Source.OUTPUT)
    MessageChannel output();
}
