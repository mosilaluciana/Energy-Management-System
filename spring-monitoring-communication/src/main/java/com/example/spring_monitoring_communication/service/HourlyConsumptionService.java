package com.example.spring_monitoring_communication.service;

import com.example.spring_monitoring_communication.DTO.HourlyConsumptionDTO;
import com.example.spring_monitoring_communication.controller.NotificationController;
import com.example.spring_monitoring_communication.entity.Device;
import com.example.spring_monitoring_communication.entity.HourlyConsumption;
import com.example.spring_monitoring_communication.entity.Measurement;
import com.example.spring_monitoring_communication.repository.DeviceRepository;
import com.example.spring_monitoring_communication.repository.HourlyConsumptionRepository;
import com.example.spring_monitoring_communication.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;


/*Acest serviciu procesează și stochează măsurători energetice,
calculând consumul total pe oră pentru fiecare dispozitiv.
Salvează măsurătorile și actualizează sau creează înregistrări
de consum orar în baza de date. Este utilizat pentru monitorizarea
eficientă a consumului energetic și generarea de notificări.
 */




@Service
@RequiredArgsConstructor
public class HourlyConsumptionService {


    private final HourlyConsumptionRepository hourlyConsumptionRepository;
    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationController notificationController;


    public void processAndStoreHourlyConsumption(UUID deviceId, long timestamp) {
        // Definește intervalul orar bazat pe timestamp
        long startOfInterval = timestamp - (timestamp % 3600000); // Rotunjire la începutul orei
        long endOfInterval = startOfInterval + 3600000; // Sfârșitul orei

        // Obține măsurătorile pentru intervalul specificat
        List<Measurement> hourlyMeasurements = measurementRepository.findByDeviceIdAndTimestampBetween(
                deviceId, startOfInterval, endOfInterval);

        if (!hourlyMeasurements.isEmpty()) {
            // Calculează consumul total pentru intervalul specificat
            float totalHourlyConsumption = hourlyMeasurements.stream()
                    .map(Measurement::getMeasurementValue)
                    .reduce(0f, Float::sum);

            // Găsește înregistrarea de consum orar corespunzătoare, dacă există
            HourlyConsumption existingHourlyConsumption = hourlyConsumptionRepository.findByDeviceIdAndTimestamp(deviceId, startOfInterval);

            if (existingHourlyConsumption == null) {
                // Creează o nouă înregistrare de consum orar
                HourlyConsumption hourlyConsumption = new HourlyConsumption();
                hourlyConsumption.setDeviceId(deviceId);
                hourlyConsumption.setTimestamp(startOfInterval); // Timpul de început al intervalului
                hourlyConsumption.setHourlyConsumption(totalHourlyConsumption);
                hourlyConsumptionRepository.save(hourlyConsumption);
            } else {
                // Actualizează înregistrarea existentă
                existingHourlyConsumption.setHourlyConsumption(totalHourlyConsumption);
                hourlyConsumptionRepository.save(existingHourlyConsumption);
            }


            //Conexiune webSocket pentru a trimite notificări
            Device device = deviceRepository.findById(deviceId).orElse(null);
            if (device != null && totalHourlyConsumption > device.getMaxHrEnCon()) {
                // Notifica utilizatorul dacă consumul orar depășește limita maxima
                String message = "Hourly consumption exceeded the maximum limit for deviceId:  " + deviceId;
                notificationController.notifyClients(device.getUserId(), message);
            }
        }

    }


    public List<HourlyConsumptionDTO> getAllDeviceMeasurements(UUID userId, long timestamp) {
        // Timpul de început și sfârșit al zilei
        long startOfDay = timestamp;
        long endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1;

        // Obține dispozitivele utilizatorului
        List<Device> devices = getAllUserDevices(userId);
        if (devices.isEmpty()) {
            return Collections.emptyList(); // Niciun dispozitiv asociat utilizatorului
        }

        // Inițializează consumul orar
        Float[] hourlyConsumption = new Float[24];
        Arrays.fill(hourlyConsumption, 0.0f);

        // Agregare măsurători
        for (Device device : devices) {
            List<Measurement> measurements = measurementRepository.findAllByDeviceIdAndTimestampBetween(
                    device.getId(), startOfDay, endOfDay
            );

            if (measurements != null) {
                for (Measurement measurement : measurements) {
                    // Calculăm ora din timestamp
                    int hourIndex = Instant.ofEpochMilli(measurement.getTimestamp())
                            .atZone(ZoneOffset.UTC)
                            .getHour();

                    // Adăugăm consumul la ora corespunzătoare
                    hourlyConsumption[hourIndex] += measurement.getMeasurementValue();
                }
            }
        }

        // Convertim rezultatele în DTO-uri
        List<HourlyConsumptionDTO> result = new ArrayList<>();
        for (int i = 0; i < hourlyConsumption.length; i++) {
            result.add(new HourlyConsumptionDTO(String.format("%02d:00", i), hourlyConsumption[i]));
        }

        return result;
    }

    private List<Device> getAllUserDevices(UUID userId) {
        return deviceRepository.findAllByUserId(userId);
    }

}