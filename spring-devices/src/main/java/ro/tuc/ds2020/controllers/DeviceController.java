package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.services.DeviceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO deviceDetailsDTO) {
        UUID deviceID = deviceService.insert(deviceDetailsDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

//    @PostMapping()
//    public ResponseEntity<UUID> insertDevice(
//            @Valid @RequestBody DeviceDetailsDTO deviceDetailsDTO,
//            @RequestParam String email) {
//        // Call the DeviceService to handle insertion with email lookup
//        UUID deviceID = deviceService.insert(deviceDetailsDTO);//, email);
//        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
//    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id") UUID deviceID) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(deviceID);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/device/id/{id}")
    public ResponseEntity<DeviceDTO> updateDeviceById(@PathVariable UUID id, @RequestBody DeviceDetailsDTO deviceDetailsDTO) {
        DeviceDTO updatedDevice = deviceService.updateDeviceById(id, deviceDetailsDTO);
        return ResponseEntity.ok(updatedDevice);
    }


    @DeleteMapping("/device/id/{id}")
    public ResponseEntity<String> deleteDeviceByID(@PathVariable UUID id) {
        boolean isDeleted = deviceService.deleteDeviceByID(id);

        if (isDeleted) {
            return ResponseEntity.ok("Device deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device not found.");
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteDevicesByUserId(@PathVariable UUID userId) {
        deviceService.deleteDevicesByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    //@GetMapping("/person/{personId}")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeviceDetailsDTO>> getDevicesByUserId(@PathVariable UUID userId) {
        List<Device> devices = deviceService.getDevicesByUserId(userId);
        List<DeviceDetailsDTO> deviceDetailsDTOs = devices.stream()
                .map(DeviceBuilder::toDeviceDetailsDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(deviceDetailsDTOs);
    }


    @DeleteMapping("/user-reference/{userReferenceId}")
    public ResponseEntity<Void> deleteDevicesByUserReferenceId(@PathVariable UUID userReferenceId) {
        deviceService.deleteDevicesByUserReferenceId(userReferenceId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user-reference/{userReferenceId}")
    public ResponseEntity<List<Device>> getDevicesByUserReferenceId(@PathVariable UUID userReferenceId) {
        List<Device> devices = deviceService.getAllDevicesByUserReferenceId(userReferenceId);
        return ResponseEntity.ok(devices);
    }

}
