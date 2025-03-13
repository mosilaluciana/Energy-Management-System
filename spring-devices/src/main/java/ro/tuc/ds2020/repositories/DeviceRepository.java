package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Device;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {


    List<Device> findByAddress(String userEmail);
    List<Device> findByDescription(String description);

    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT p " +
            "FROM Device p " +
            "WHERE p.description = :descriptions " +
            "AND p.maxHrEnCon >= 1  ")
    Optional<Device> findDeviceByDescription(@Param("description") String description);
    Optional<Device> findDeviceById(UUID id);
    List<Device> findByUserReferenceId(UUID userReferenceId);
    List<Device> findByUserReference_UserId(UUID userId);
    List<Device> findAllByUserReferenceId(UUID userReferenceId);
}
