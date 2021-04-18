package br.com.supernova.ecommercemicroservice.service;

import br.com.supernova.ecommercemicroservice.avro.checkout.CheckoutEventSource;
import br.com.supernova.ecommercemicroservice.config.message.MessagingPort;
import br.com.supernova.ecommercemicroservice.dto.CheckoutEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckoutServiceStream implements MessagingPort<CheckoutEventSource> {

    @Autowired
    @Qualifier(value = "checkoutProducer")
    private KafkaProducer<String, CheckoutEventSource> producer;

    @Override
    public String topic() {
        return "checkout-created-output";
    }

    @Override
    public ProducerRecord<String, CheckoutEventSource> createProducerRecord(CheckoutEventSource eventSource) {
        return new ProducerRecord<String, CheckoutEventSource>(this.topic(), eventSource);
    }

    @Override
    public void send(CheckoutEventDTO eventDTO) {
        log.info("Creating wrapper for shipping");
        CheckoutEventSource eventSource = CheckoutEventSource.newBuilder()
                .setCheckoutCode(eventDTO.getCode())
                .setStatus(eventDTO.getStatus().name())
                .build();

        log.info("Performing check for sending data");
        producer.send(this.createProducerRecord(eventSource), (rm, ex) -> {
            if(ex == null){
                log.info("Data sent with success!");
            } else {
                log.error("Fail to send message!");
            }
        });

        log.info("Sending stream");
        producer.flush();
        log.info("closing connection with messaging");
        producer.close();
    }
}
