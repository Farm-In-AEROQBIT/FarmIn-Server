package com.farmin.farminserver.entity.barns.piglet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.farmin.farminserver.entity.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Piglet")
public class PigletEntity extends BaseEntity{
    @Column(nullable = false, length = 50)
    private String FarmID;

    @Column(nullable = false, length = 100)
    private String PigletID;
}
