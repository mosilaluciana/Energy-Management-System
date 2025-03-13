package com.example.spring_monitoring_communication.controller;

import ch.qos.logback.core.net.HardenedObjectInputStream;
import com.example.spring_monitoring_communication.DTO.HourlyConsumptionDTO;
import com.example.spring_monitoring_communication.service.HourlyConsumptionService;
import com.example.spring_monitoring_communication.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping ("/api/measurement")
@RequiredArgsConstructor
@CrossOrigin(origins ="localhost:3000")
public class MeasurementController {

    private final MeasurementService measurementService;

   // @GetMapping ("/graphData/{timestamp}/{userId}")
    //public List<HourlyConsumptionDTO> getAllDeviceMeasurements(@PathVariable("timestamp") String timestamp, @PathVariable("userId") UUID userId) throws ParseException{
        //List<HourlyConsumptionDTO> allMeasurements = measurementService.getAllDeviceMeasurements(timestamp, userId);
      //  return allMeasurements;

   // }

//
//    @PostMapping("/energy-consumption")
//    public ResponseEntity<HourlyConsumptionDTO> getEnergyConsumption(@RequestBody Map<String, String> requestData) {
//        String userId = requestData.get("userId");
//        String dateString = requestData.get("date");
//
//        if (userId == null || dateString == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        try {
//            long timestamp = DateUtil.transformToEpochMillis(dateString);
//
//            // Fetch consumption data
//            HourlyConsumptionDTO consumption = (HourlyConsumptionDTO) HourlyConsumptionService.getAllDeviceMeasurements(UUID.fromString(userId), timestamp);
//
//            return ResponseEntity.ok(consumption);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }





}
