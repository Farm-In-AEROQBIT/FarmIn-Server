package com.farmin.farminserver.entity.catm1.reservecatm1sensor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ReserveCatm1Sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveCatm1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SensorIdc")
    private Integer sensorIdc;

    @Column(name = "ReserveID", nullable = false)
    private Integer reserveID;

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
