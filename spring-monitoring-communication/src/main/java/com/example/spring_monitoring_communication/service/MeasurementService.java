package com.example.spring_monitoring_communication.service;

import com.example.spring_monitoring_communication.entity.Device;

import com.example.spring_monitoring_communication.repository.DeviceRepository;
import com.example.spring_monitoring_communication.repository.MeasurementRepository;
import com.example.spring_monitoring_communication.repository.HourlyConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;



}