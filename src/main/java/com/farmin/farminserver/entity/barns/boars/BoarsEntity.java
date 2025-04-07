package com.farmin.farminserver.entity.barns.boars;

import jakarta.persistence.*;
import lombok.*;
import com.farmin.farminserver.entity.BaseEntity;

@Getter
@Entity
@Table(name = "Boars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoarsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BoarsID")
    private Integer BoarsId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
