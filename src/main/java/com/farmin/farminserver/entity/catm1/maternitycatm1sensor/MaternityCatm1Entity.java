package com.farmin.farminserver.entity.catm1.maternitycatm1sensor;

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
@Table(name = "MaternityCatm1Sensor")
public class MaternityCatm1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 사용
    @Column(name = "SensorID")
    private Integer sensorID; // Integer로 정의

    @Column(name = "MaternityID", nullable = false)
    private String maternityID;

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
