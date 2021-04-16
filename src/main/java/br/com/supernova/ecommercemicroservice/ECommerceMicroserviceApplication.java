package br.com.supernova.ecommercemicroservice;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class ECommerceMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceMicroserviceApplication.class, args);
	}

}
