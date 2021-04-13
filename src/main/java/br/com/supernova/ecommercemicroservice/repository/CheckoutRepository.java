package br.com.supernova.ecommercemicroservice.repository;

import br.com.supernova.ecommercemicroservice.entity.CheckoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutRepository extends JpaRepository<CheckoutEntity, Long> {
    Optional<CheckoutEntity> findByCode(String code);
}
