package br.com.supernova.ecommercemicroservice.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
@NoArgsConstructor
public class KafkaProperties {

    private List<String> bootstrapServers;
    private String acksConfig;
    private String retriesConfig;
    private Class<?> keySerializer = StringSerializer.class;
    private Class<?> valueSerializer = KafkaAvroSerializer.class;
    private String schemaRegistryUrl;
}
