package com.farmin.farminserver.entity.barns.growing;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "Growing")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GrowingID")
    private Integer growingId;

    @Column(name = "SNFarmID", nullable = false, length = 100)
    private String snFarmId;
}
