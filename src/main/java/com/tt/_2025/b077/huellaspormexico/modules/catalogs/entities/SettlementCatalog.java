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

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@Entity
@Table(name = "settlements")
@AllArgsConstructor
@NoArgsConstructor
public class SettlementCatalog extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String settlement;

    @Column(name = "sustainability_index", nullable = false)
    private BigDecimal sustainabilityIndex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StateCatalog state;
}
