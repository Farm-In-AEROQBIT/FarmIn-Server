package com.farmin.farminserver.entity.catm1.finishingcatm1sensor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "FinishingCatm1Sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinishingCatm1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SensorIdc")
    private Integer sensorIdc;

    @Column(name = "FinishingID", nullable = false)
    private Integer finishingId;

    @Column(name = "Temper")
    private String temper;

    @Column(name = "WTemper")
    private String wtemper;

    @Column(name = "Humidity")
    private String humidity;

    @Column(name = "Co2")
    private String co2;

    @Column(name = "Time")
    private LocalDateTime time;
}

