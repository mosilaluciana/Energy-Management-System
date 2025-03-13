package com.example.spring_monitoring_communication.controller;

import com.example.spring_monitoring_communication.DTO.HourlyConsumptionDTO;
import com.example.spring_monitoring_communication.DateAndStringRequest;
import com.example.spring_monitoring_communication.service.HourlyConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceController {


    private final HourlyConsumptionService hourlyConsumptionService;


    @PostMapping("/send-date-and-string")
    public ResponseEntity<?> receiveDateAndString(@RequestBody DateAndStringRequest request) throws ParseException {
        // Procesăm data
        long timestamp;
        try {
            LocalDate localDate = LocalDate.parse(request.getDate());
            timestamp = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format");
        }

        // Procesăm userId (care este trimis ca string)
        UUID userId;
        try {
            userId = UUID.fromString(request.getString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format");
        }

        System.out.println("userId:" + userId + " timestamp: " + timestamp);

        // Obținem măsurătorile
        List<HourlyConsumptionDTO> list;

       // list = hourlyConsumptionService.getAllDeviceMeasurements(userId, timestamp);
       // System.out.println("list: " + list);


        try {
            list = hourlyConsumptionService.getAllDeviceMeasurements(userId, timestamp);
            System.out.println("list: " + list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving data");
        }

        // Verificăm dacă lista e goală
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found for the given parameters");
        }

        // Răspuns de succes cu date serializate
        return ResponseEntity.ok(list);
    }






}