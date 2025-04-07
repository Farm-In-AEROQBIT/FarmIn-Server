package com.farmin.farminserver.entity.barns.gestation;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "Gestation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GestationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GestationID")
    private Integer gestationId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
