package com.example.spring_monitoring_communication.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "measurement")
public class Measurement {


    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "device_id", columnDefinition = "uuid")
    private UUID deviceId;

    @Column(name = "measurement_value")
    private float measurementValue;

}
