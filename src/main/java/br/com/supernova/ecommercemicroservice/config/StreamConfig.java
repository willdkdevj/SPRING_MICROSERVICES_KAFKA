package br.com.supernova.ecommercemicroservice.config;

import br.com.supernova.ecommercemicroservice.stream.Source;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(
        value = {Source.class}
)
public class StreamConfig {
}
