package com.example.spring_monitoring_communication.repository;

import com.example.spring_monitoring_communication.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {


    Optional<Device> findDeviceById(UUID uuid);
    List<Device> findAllByUserId(UUID uuid);

}
