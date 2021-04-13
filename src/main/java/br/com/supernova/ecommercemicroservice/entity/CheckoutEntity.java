package br.com.supernova.ecommercemicroservice.entity
        ;

import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String code;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusCheckoutEnum status;

    @Column
    private Boolean saveAddress;

    @Column
    private Boolean saveInformation;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    private ShippingEntity shipping;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL)
    private List<CheckoutItemEntity> items;
}
