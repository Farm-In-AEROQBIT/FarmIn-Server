package com.farmin.farminserver.entity.snfarminfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SNFarmInfo")
public class SNFarmInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SNFarmID")
    private String snFarmId;

    @Column(name = "FarmID", nullable = false)
    private int farmId;
}