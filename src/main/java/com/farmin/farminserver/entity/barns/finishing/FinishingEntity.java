package com.farmin.farminserver.entity.barns.finishing;

import com.farmin.farminserver.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Finishing")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinishingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FinishingID")
    private Integer finishingId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
