package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.DeviceMessage;
import ro.tuc.ds2020.rabbitMQ.RabbitMQProducer;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserReferenceRepository userReferenceRepository;

    @Autowired
    private RestTemplate restTemplate;
    UserReferenceService userReferenceService;

    //RabbitMQ
    public final RabbitMQProducer messageProducer;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserReferenceRepository userReferenceRepository, RabbitMQProducer messageProducer) {
        this.deviceRepository = deviceRepository;
        this.userReferenceRepository = userReferenceRepository;
        this.messageProducer = messageProducer;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> userList = deviceRepository.findAll();
        return userList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDetailsDTO(deviceOptional.get());
    }

    @Transactional
    public UUID insert(DeviceDetailsDTO deviceDTO) {

        Device device = DeviceBuilder.toEntity(deviceDTO, userReferenceRepository);

        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());


        //RabbitMQ  - send the deviceMessage to queue
        DeviceMessage deviceMessage = new DeviceMessage(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHrEnCon(), device.getUserReference().getUserId(), "insert");
        messageProducer.sendMessage(deviceMessage);

        return device.getId();
    }


    @Transactional
    public DeviceDTO updateDeviceById(UUID id, DeviceDetailsDTO deviceDetailsDTO) {
        Optional<Device> existingDevice = deviceRepository.findById(id);

        if(existingDevice.isPresent()) {
            Device device = existingDevice.get();
            System.out.println(device);
        
        device.setAddress(deviceDetailsDTO.getAddress());
        device.setDescription(deviceDetailsDTO.getDescription());
        device.setMaxHrEnCon(deviceDetailsDTO.getMaxHrEnCon());

        deviceRepository.save(device);
        LOGGER.info("Device with id {} was updated in db", device.getId());

        //RabbitMQ  - send the deviceMessage to queue for updates
        DeviceMessage deviceMessage = new DeviceMessage(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHrEnCon(), device.getUserReference().getUserId(), "update");
        messageProducer.sendMessage(deviceMessage);

        return DeviceBuilder.toDeviceDTO(device);

        }

        return null;
    }

    
    public boolean deleteDeviceByID(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isPresent()) {

            Device device = deviceOptional.get();
            deviceRepository.delete(deviceOptional.get());

            //RabbitMQ  - send the deviceMessage to queue for delete
            DeviceMessage deviceMessage = new DeviceMessage(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHrEnCon(), device.getUserReference().getUserId(), "delete");
            messageProducer.sendMessage(deviceMessage);

            LOGGER.info("Device with id {} was deleted from db", id);

            return true;
        }
        return false;
    }


    public List<Device> getAllDevicesByUserReferenceId(UUID userReferenceId) {
        return deviceRepository.findAllByUserReferenceId(userReferenceId);
    }

    public List<Device> findDevicesByUserReferenceId(UUID userReferenceId) {
        List<Device> devices = deviceRepository.findAllByUserReferenceId(userReferenceId);

        if (devices.isEmpty()) {
            LOGGER.warn("No devices found for userReferenceId: {}", userReferenceId);
            throw new ResourceNotFoundException("No devices found for userReferenceId: " + userReferenceId);
        }

        LOGGER.info("Found {} devices for userReferenceId: {}", devices.size(), userReferenceId);
        return devices;
    }

    @Transactional
    public void deleteDevicesByUserReferenceId(UUID userReferenceId) {
        List<Device> devices = deviceRepository.findAllByUserReferenceId(userReferenceId);

        if (devices.isEmpty()) {
            LOGGER.warn("No devices found for userReferenceId: {}", userReferenceId);
            throw new ResourceNotFoundException("No devices found for userReferenceId: " + userReferenceId);
        }

        deviceRepository.deleteAll(devices);
        LOGGER.info("Deleted {} devices for userReferenceId: {}", devices.size(), userReferenceId);
    }
    
    

    public List<Device> getDevicesByUserId(UUID userId) {
        return deviceRepository.findByUserReference_UserId(userId);
    }

    public void deleteDevicesByUserId(UUID userID){
        List<Device> devices =deviceRepository.findByUserReference_UserId(userID);
        deviceRepository.deleteAll(devices);


    }


}
