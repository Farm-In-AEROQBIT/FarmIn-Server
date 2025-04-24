package com.farmin.farminserver.entity.snfarminfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SNFarmInfo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SNFarmInfoEntity {

    @Id
    @Column(name = "SNFarmID")
    private String snFarmId;

    @Column(name = "FarmID", nullable = false)
    private int farmId;
}