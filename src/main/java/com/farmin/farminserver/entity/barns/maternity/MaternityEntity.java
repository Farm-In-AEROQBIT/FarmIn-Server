package com.farmin.farminserver.entity.barns.maternity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "Maternity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaternityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaternityID")
    private Integer maternityId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
