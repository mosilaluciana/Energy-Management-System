package com.simulator.controller;
import com.simulator.model.CSVReaderComponent;
import com.simulator.model.JSONCreatorComponent;
import com.simulator.rabbitMQ.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;


@Controller
public class SimulatorController {

//    @Value("${sensor.device.uuid}")
//    private  String deviceId;

    private JSONCreatorComponent jsonCreator;
    private List<Double> energyData; // FROM CSVReader
    private Sender sender;
    private int dataIndex = 0;

    @Autowired
    public SimulatorController(CSVReaderComponent csvReader, JSONCreatorComponent jsonCreator, Sender sender) {
        this.jsonCreator = jsonCreator;
        this.sender = sender;
        this.energyData = csvReader.readEnergyData();
    }


    @Scheduled(fixedRate = 7000) // Every ... sec
    public void simulateAndSendData() {
        if (dataIndex < energyData.size()) {
            Double measurementValue = energyData.get(dataIndex++);
            String jsonMessage = jsonCreator.createJSONMessage( measurementValue);
            sender.sendMessage(jsonMessage);
        }
    }
}

