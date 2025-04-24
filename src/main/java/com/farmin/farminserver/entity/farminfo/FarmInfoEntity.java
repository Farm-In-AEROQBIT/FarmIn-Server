package com.farmin.farminserver.entity.farminfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.farmin.farminserver.entity.BaseEntity;

@Entity
@Table(name = "FarmInfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer farmId;

    @Column(nullable = false, length = 100)
    private String farmName;

    @Column(nullable = false)
    private Integer userId;
}