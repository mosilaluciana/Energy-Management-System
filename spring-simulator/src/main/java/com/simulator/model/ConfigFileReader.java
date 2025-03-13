package com.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Component
@Data

@NoArgsConstructor
public class ConfigFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFileReader.class);

    public UUID convertStringToUUID(String filePath) {
        try {
            Path path = Paths.get(filePath);
            LOGGER.info("Reading UUID from file: " + path.toAbsolutePath());
            String content = Files.readString(path).trim();

            return UUID.fromString(content);  //arunca o excepție dacă nu este un UUID valid
        } catch (IOException e) {
            LOGGER.error("Error reading config file: " + filePath, e);
            throw new RuntimeException("Error reading config file: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid UUID format in config file: " + filePath, e);
            throw new RuntimeException("Invalid UUID format in config file.", e);
        }
    }

}