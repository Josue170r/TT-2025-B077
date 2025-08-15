package com.tt._2025.b077.huellaspormexico.modules.catalogs.entities;

import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@Entity
@Table(name = "postal_codes")
@AllArgsConstructor
@NoArgsConstructor
public class PostalCodeCatalog extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postal_code", nullable = false, length = 5)
    private String postalCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "settlement_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SettlementCatalog settlement;
}
