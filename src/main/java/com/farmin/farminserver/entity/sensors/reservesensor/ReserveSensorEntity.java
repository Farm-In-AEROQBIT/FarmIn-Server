package com.farmin.farminserver.entity.sensors.reservesensor;

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
@Table(name = "ReserveSensor")
public class ReserveSensorEntity extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String ReserveID;

    @Column(nullable = false, length = 50)
    private String SensorID;

    @Column(nullable = false, length = 50)
    private String Co2;

    @Column(nullable = false, length = 50)
    private String Nh3;

    @Column(nullable = false, length = 50)
    private String PM;

    @Column(nullable = false, length = 50)
    private String Temper;

    @Column(nullable = false, length = 50)
    private String Humidity;

}
