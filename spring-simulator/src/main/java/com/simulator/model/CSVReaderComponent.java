package com.simulator.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVReaderComponent {

    @Value("${sensor.path}")
    private String path;

    public List<Double> readEnergyData() {
        List<Double> data = new ArrayList<>();

        // Use the class loader to load the resource as a stream
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                System.out.println("Failed to load file from path: " + path);
            }
            assert inputStream != null;  // This will throw an exception if inputStream is null
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    data.add(Double.parseDouble(line));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
