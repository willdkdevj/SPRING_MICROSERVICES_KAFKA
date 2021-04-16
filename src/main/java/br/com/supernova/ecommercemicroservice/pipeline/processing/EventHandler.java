package br.com.supernova.ecommercemicroservice.pipeline.processing;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import br.com.supernova.ecommercemicroservice.streaming.CheckoutEventStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
@Slf4j
public class EventHandler {

    @Bean
    public BiFunction<KStream<String, CheckoutEventStream>, KTable<String, CheckoutEventStream>, KStream<String, CheckoutEventStream>> processEvent() {
        return (eventStream, checkoutTable) -> eventStream
                .leftJoin(checkoutTable, (event, checkoutEvent) -> null == checkoutEvent ? event : null)
                .filter((str, event) -> {
                    if(null != event)
                        log.info("Sending checkout event {}", event);
                    return null != event;
                });
    }
}
