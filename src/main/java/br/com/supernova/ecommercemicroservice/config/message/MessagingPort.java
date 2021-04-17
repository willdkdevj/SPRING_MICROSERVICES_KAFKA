package br.com.supernova.ecommercemicroservice.config.message;

import br.com.supernova.ecommercemicroservice.resource.checkout.CheckoutResource;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface MessagingPort<T extends SpecificRecordBase> {

    String topic();
    ProducerRecord<String, T> createProducerRecord(T type);
    void send(CheckoutResource resource);
}
