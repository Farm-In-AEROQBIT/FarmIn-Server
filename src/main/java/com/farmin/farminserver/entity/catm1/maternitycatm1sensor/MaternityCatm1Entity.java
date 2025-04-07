package com.farmin.farminserver.entity.catm1.maternitycatm1sensor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MaternityCatm1Sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaternityCatm1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SensorIdc")
    private Integer sensorIdc;

    @Column(name = "MaternityID", nullable = false)
    private Integer maternityID;

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
