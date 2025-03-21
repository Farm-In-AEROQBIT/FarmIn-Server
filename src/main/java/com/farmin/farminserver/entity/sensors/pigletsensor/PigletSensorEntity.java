package com.farmin.farminserver.entity.sensors.pigletsensor;

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
@Table(name = "PigletSensor")
public class PigletSensorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 사용
    @Column(name = "SensorID")
    private Integer sensorID; // Integer로 정의

    @Column(name = "PigletID", nullable = false)
    private String pigletID;

    @Column(name = "Co2")
    private String co2;

    @Column(name = "Nh3")
    private String nh3;

    @Column(name = "PM")
    private String pm;

    @Column(name = "Temper")
    private String temper;

    @Column(name = "Humidity")
    private String humidity;

    @Column(name = "Time")
    private String time;
}
