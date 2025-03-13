package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {

        UserReference userReference = device.getUserReference();
        UserReferenceDTO userReferenceDTO = null;

        if (userReference != null) {
            userReferenceDTO = new UserReferenceDTO(userReference.getUserId());
        }
        return new DeviceDTO(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHrEnCon(), userReferenceDTO);

    }

    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        UserReference userReference = device.getUserReference();
        UserReferenceDTO userReferenceDTO = null;

        if (userReference != null) {
            userReferenceDTO = new UserReferenceDTO(userReference.getUserId());
        }
        return new DeviceDetailsDTO(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHrEnCon(), userReferenceDTO.getUserReference());

    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO, UserReferenceRepository userReferenceRepository) {

        UserReference userReference = userReferenceRepository.findByUserId(deviceDetailsDTO.getUserReference().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("UserReference with userId: " + deviceDetailsDTO.getUserReference().getUserId() + " does not exist"));

        return new Device(deviceDetailsDTO.getDescription(),
                deviceDetailsDTO.getAddress(),
                deviceDetailsDTO.getMaxHrEnCon(),
                userReference);
    }

    private static void orElseThrow(Object o) {
    }
}
