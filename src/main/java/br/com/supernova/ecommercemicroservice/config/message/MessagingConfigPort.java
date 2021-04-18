package br.com.supernova.ecommercemicroservice.config.message;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface MessagingConfigPort<T extends SpecificRecordBase> {

    Producer<String, T> configureProducer();

}
