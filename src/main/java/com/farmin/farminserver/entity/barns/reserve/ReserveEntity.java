package com.farmin.farminserver.entity.barns.reserve;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "Reserve")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReserveID")
    private Integer reserveId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
