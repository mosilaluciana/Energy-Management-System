package com.example.spring_monitoring_communication.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hourly_consumption")
public class HourlyConsumption {

    @Id
    @Column(name = "device_id", columnDefinition = "uuid")
    private UUID deviceId;
    private Long timestamp; // Ora pentru care se calculează consumul
    private float  hourlyConsumption;


}
