package ro.tuc.ds2020.dtos.builders;

        import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
        import ro.tuc.ds2020.dtos.DeviceDTO;
        import ro.tuc.ds2020.dtos.DeviceMessageDTO;
        import ro.tuc.ds2020.dtos.DeviceMessageDTO;
        import ro.tuc.ds2020.dtos.UserReferenceDTO;
        import ro.tuc.ds2020.entities.Device;
        import ro.tuc.ds2020.entities.DeviceMessage;

public class DeviceMessageBuilder {

    private DeviceMessageBuilder() {
    }

    public static DeviceMessageDTO toDeviceMessageDTO(DeviceMessage device) {

        return new DeviceMessageDTO(device.getId(), device.getDescription(), device.getAddress(), device.getMaxHrEnCon(), device.getUserId(), device.getMethod());

    }

    public static DeviceMessage toEntity(DeviceMessageDTO deviceMessageDTO) {

        return new DeviceMessage(deviceMessageDTO.getDescription(),
                deviceMessageDTO.getAddress(),
                deviceMessageDTO.getMaxHrEnCon(),
                deviceMessageDTO.getUserId(),
                deviceMessageDTO.getMethod());
    }

    private static void orElseThrow(Object o) {
    }
}
