package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.config.message.MessagingPort;
import br.com.supernova.ecommercemicroservice.avro.checkout.CheckoutEventSource;
import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckoutServiceStream implements MessagingPort<CheckoutEventSource> {

    private final KafkaProducer<String, CheckoutEventSource> producer;

    @Override
    public String topic() {
        return "checkout-created-output";
    }

    @Override
    public ProducerRecord<String, CheckoutEventSource> createProducerRecord(CheckoutEventSource eventSource) {
        return new ProducerRecord<String, CheckoutEventSource>(this.topic(), eventSource);
    }

    @Override
    public void send(CheckoutResource resource) {
        CheckoutEventSource eventSource = CheckoutEventSource.newBuilder()
                .setName(((CheckoutResource) resource).getName())
                .setStatus(((CheckoutResource) resource).getStatus().name())
                .build();
        producer.send(this.createProducerRecord(eventSource), (rm, ex) -> {
            if(ex == null){
                log.info("Data sent with success!");
            } else {
                log.error("Fail to send message");
            }
        });

        producer.flush();
        producer.close();
    }
}
