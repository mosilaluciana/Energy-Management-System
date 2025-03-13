package com.simulator.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;

@Component
public class JSONCreatorComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONCreatorComponent.class);
    private ConfigFileReader configFileReader;
    private ObjectMapper objectMapper;

    //@Value("${sensor.device.uuid}")
    private UUID deviceId;

    public JSONCreatorComponent(ObjectMapper objectMapper, ConfigFileReader configFileReader, @Value("${sensor.config.file.path}") String configFilePath) {
        this.objectMapper = objectMapper;
        this.configFileReader = configFileReader;
        this.deviceId = configFileReader.convertStringToUUID(configFilePath); // Citește UUID-ul din fișier
    }

    public String createJSONMessage(Double measurementValue) {
        Map<String, Object> message = new HashMap<>();
        message.put("timestamp", System.currentTimeMillis());
        message.put("deviceId", deviceId);
        message.put("measurementValue", measurementValue);
        LOGGER.info("Sent Message: " + message);
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

