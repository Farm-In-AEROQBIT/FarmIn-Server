package com.farmin.farminserver.entity.catm1.growingcatm1sensor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GrowingCatm1Sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowingCatm1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SensorIdc")
    private Integer sensorIdc;

    @Column(name = "GrowingID", nullable = false)
    private Integer growingID;

    @Column(name = "Temper")
    private String temper;

    @Column(name = "WTemper")
    private String wtemper;

    @Column(name = "Humidity")
    private String humidity;

    @Column(name = "Co2")
    private String co2;

    @Column(name = "Time")
    private String time;
}

