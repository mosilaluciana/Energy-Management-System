//package com.example.spring_monitoring_communication.rabbitMQ;
//
//import com.example.spring_monitoring_communication.entity.Device;
//import com.example.spring_monitoring_communication.entity.HourlyConsumption;
//import com.example.spring_monitoring_communication.entity.Measurement;
//import com.example.spring_monitoring_communication.repository.DeviceRepository;
//import com.example.spring_monitoring_communication.repository.HourlyConsumptionRepository;
//import com.example.spring_monitoring_communication.repository.MeasurementRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class MessageReceiver {
//
//    private final MeasurementRepository measurementRepository;
//    private final DeviceRepository deviceRepository;
//    private final HourlyConsumptionRepository hourlyConsumptionRepository;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//
//@RabbitListener(queues = "${rabbitmq.queue.device.name}")
//public void processDeviceMessageDevice(String message) throws JsonProcessingException {
//
//    System.out.println("Received message: " + message);
//
//    if (message.startsWith("\"") && message.endsWith("\"")) {
//        message = message.substring(1, message.length() - 1);
//    }
//    message = message.replace("\\", "");
//
//    System.out.println("Cleaned Message: " + message);
//
//    // Extract the part after the "method" key
//    String method = null;
//    int methodIndex = message.indexOf("\"method\":");
//    if (methodIndex != -1) {
//        int methodStart = methodIndex + "\"method\":\"".length();
//        int methodEnd = message.indexOf("\"", methodStart);
//        if (methodEnd != -1) {
//            method = message.substring(methodStart, methodEnd);
//
//        }
//    }
//
//    message = message.replace(",\"method\":\"" + method + "\"", "");
//
//    System.out.println("Processed JSON message: " + message);
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    Device device = objectMapper.readValue(message, Device.class);
//
//    System.out.println("Mapped Device object: " + device);
//
//    // Save the device to the database
//    deviceRepository.save(device);
//    System.out.println("Saved device: " + device);
//}
//
//
//
//
//
//
//
//    @RabbitListener(queues = "${rabbitmq.queue.sensor.name}")
//    public void processMessageSimulator(String message) throws JsonProcessingException {
//
//
//        System.out.println("Received message: " + message);
//
//        if (message.startsWith("\"") && message.endsWith("\"")) {
//            message = message.substring(1, message.length() - 1);
//        }
//        message = message.replace("\\", "");
//
//        System.out.println("Cleaned Message: " + message);
//
//
//        // Deserialize message into a Measurement object
//        Measurement measurement = objectMapper.readValue(message, Measurement.class);
//
//        // Save the measurement to the database
//        measurementRepository.save(measurement);
//
//        // Retrieve the last 6 measurements for this device
//        List<Measurement> dbMeasurements = measurementRepository.findByDeviceId(measurement.getDeviceId());
//
//        if (dbMeasurements.size() >= 6) {
//            // Calculate the average of the last 6 measurement values
//            Float total = dbMeasurements.subList(dbMeasurements.size() - 6, dbMeasurements.size())
//                    .stream()
//                    .map(Measurement::getMeasurementValue)
//                    .reduce(0f, Float::sum) / 6;
//
//
//            // Create and save the hourly consumption
//            HourlyConsumption hourlyConsumption = new HourlyConsumption();
//            hourlyConsumption.setDeviceId(measurement.getDeviceId());
//            hourlyConsumption.setHourlyConsumption(total);
//            hourlyConsumptionRepository.save(hourlyConsumption);
//
//            System.out.println("Saved hourly consumption: " + hourlyConsumption);
//        }
//    }
//
//
//}


package com.example.spring_monitoring_communication.rabbitMQ;

import com.example.spring_monitoring_communication.entity.Device;
import com.example.spring_monitoring_communication.entity.HourlyConsumption;
import com.example.spring_monitoring_communication.entity.Measurement;
import com.example.spring_monitoring_communication.repository.DeviceRepository;
import com.example.spring_monitoring_communication.repository.HourlyConsumptionRepository;
import com.example.spring_monitoring_communication.repository.MeasurementRepository;
import com.example.spring_monitoring_communication.service.HourlyConsumptionService;
import com.example.spring_monitoring_communication.service.MeasurementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MessageReceiver {


    private final DeviceRepository deviceRepository;

    private final HourlyConsumptionService hourlyConsumptionService;

    private final MeasurementRepository measurementRepository;
    private final MeasurementService measurementService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "${rabbitmq.queue.device.name}")
    public void processDeviceMessageDevice(String message) throws JsonProcessingException {
        message = cleanMessage(message);
        String method = extractMethod(message);
        message = message.replace(",\"method\":\"" + method + "\"", "");

        System.out.println("Received method: " + method);

        if(Objects.equals(method, "insert")) {
            Device device = objectMapper.readValue(message, Device.class);
            deviceRepository.save(device);
        }
        else if(Objects.equals(method, "delete")){

            Device device = objectMapper.readValue(message, Device.class);
            UUID deviceId = device.getId();
            deviceRepository.deleteById(deviceId);
        }

        else if(Objects.equals(method, "update")){

            Device updatedDevice = objectMapper.readValue(message, Device.class);
            UUID deviceId = updatedDevice.getId();
            Device existingDevice = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found"));

            existingDevice.setUserId(updatedDevice.getUserId());
            existingDevice.setDescription(updatedDevice.getDescription());
            existingDevice.setAddress(updatedDevice.getAddress());
            existingDevice.setMaxHrEnCon(updatedDevice.getMaxHrEnCon());

            deviceRepository.save(existingDevice);
        }


    }


    @RabbitListener(queues = "${rabbitmq.queue.sensor.name}")
    public void processMessageSimulator(String message) throws JsonProcessingException {
        message = cleanMessage(message);
        Measurement measurement = objectMapper.readValue(message, Measurement.class);

        // Salvează măsurătoarea curentă în baza de date
        measurementRepository.save(measurement);

        //apel la metoda consumului orar

        hourlyConsumptionService.processAndStoreHourlyConsumption(measurement.getDeviceId(),measurement.getTimestamp());

    }

    /// noua functie cu timestamp unic

    private String cleanMessage(String message) {
        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() - 1);
        }
        return message.replace("\\", "");
    }

    private String extractMethod(String message) {
        int methodIndex = message.indexOf("\"method\":");
        if (methodIndex != -1) {
            int methodStart = methodIndex + "\"method\":\"".length();
            int methodEnd = message.indexOf("\"", methodStart);
            if (methodEnd != -1) {
                return message.substring(methodStart, methodEnd);
            }
        }
        return null;
    }
}