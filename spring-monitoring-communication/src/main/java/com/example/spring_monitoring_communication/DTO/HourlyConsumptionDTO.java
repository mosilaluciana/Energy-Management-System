package com.example.spring_monitoring_communication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HourlyConsumptionDTO {

    private String hour;
    //private Long timestamp;
    private float consumption;


}
