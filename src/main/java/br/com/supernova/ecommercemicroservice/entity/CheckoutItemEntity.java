package br.com.supernova.ecommercemicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String product;

    @ManyToOne
    private CheckoutEntity checkout;
}
