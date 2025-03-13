package com.example.spring_monitoring_communication.repository;

import com.example.spring_monitoring_communication.entity.HourlyConsumption;
import com.example.spring_monitoring_communication.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, UUID> {

    HourlyConsumption findByDeviceIdAndTimestamp(UUID deviceId, long startOfHour);

    static List<Measurement> findAllByDeviceIdAndTimestampBetween(UUID id, long time, long time1) {
        return null;
    }


}
