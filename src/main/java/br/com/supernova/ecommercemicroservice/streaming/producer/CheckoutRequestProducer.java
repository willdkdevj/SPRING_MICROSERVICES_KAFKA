package br.com.supernova.ecommercemicroservice.streaming.producer;

import br.com.supernova.ecommercemicroservice.streaming.CheckoutEventSource;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

import java.util.Map;

@EnableBinding({CheckoutEventSource.class})
@RequiredArgsConstructor
public class CheckoutRequestProducer {

    private final CheckoutEventSource source;

    public void requestApproval(Map<String, Object> orderRequest) {
        source.output().send(MessageBuilder.withPayload(orderRequest).build());
    }
}
