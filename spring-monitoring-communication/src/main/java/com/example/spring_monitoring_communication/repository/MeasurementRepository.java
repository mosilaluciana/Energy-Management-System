package com.example.spring_monitoring_communication.repository;

import com.example.spring_monitoring_communication.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, UUID>{


    List<Measurement> findByDeviceIdAndTimestampBetween(UUID deviceId, long startOfHour, long endOfHour);
    List<Measurement> findAllByDeviceIdAndTimestampBetween(UUID deviceId, Long startTimestamp, Long endTimestamp);
}
