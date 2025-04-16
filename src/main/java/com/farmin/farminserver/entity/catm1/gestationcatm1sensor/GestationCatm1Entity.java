package com.farmin.farminserver.entity.catm1.gestationcatm1sensor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "GestationCatm1Sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GestationCatm1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SensorIdc")
    private Integer sensorIdc;

    @Column(name = "GestationID", nullable = false)
    private Integer gestationId;

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
