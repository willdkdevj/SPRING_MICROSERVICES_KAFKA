package br.com.supernova.ecommercemicroservice.pipeline.processing;

import br.com.supernova.ecommercemicroservice.streaming.CheckoutEventStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class StorageSinks {

    @Bean
    public Function<KStream<String, CheckoutEventStream>, KStream<String, CheckoutEventStream>> getCheckoutFromEvent(){
        return (eventStream) -> eventStream.mapValues((str, event) -> event.toBuilder().build());
    }

    @Bean
    public Consumer<KTable<String, CheckoutEventStream>> checkoutStorageSink() {
        return checkoutTable -> {
            checkoutTable.mapValues((checkoutCode, checkoutEvent) -> {
                log.info("Sinking checkout #{} to persistent state store: {} [{}]", checkoutCode,
                        checkoutTable.queryableStoreName(), checkoutEvent);
                return checkoutEvent;
            });
        };
    }

}
