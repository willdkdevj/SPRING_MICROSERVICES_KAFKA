package br.com.supernova.ecommercemicroservice.config;

import br.com.supernova.ecommercemicroservice.avro.checkout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class MessagingConfigCheckout implements MessagingConfigPort<CheckoutEventSource> {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean(name = "checkoutProducer")
    public KafkaProducer<String, CheckoutEventSource> configureProducer(){
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ACKS_CONFIG, kafkaProperties.getAcksConfig());
        properties.put(RETRIES_CONFIG, kafkaProperties.getRetriesConfig());
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializer());
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializer());
        properties.put(SCHEMA_REGISTRY_URL_CONFIG, kafkaProperties.getSchemaRegistryUrl());

        return  new KafkaProducer<String, CheckoutEventSource>(properties);
    }

}
