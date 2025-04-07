package com.farmin.farminserver.entity.barns.piglet;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "Piglet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PigletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PigletID")
    private Integer pigletId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
