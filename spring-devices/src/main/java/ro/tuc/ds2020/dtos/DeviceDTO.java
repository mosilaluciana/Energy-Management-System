package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class DeviceDTO extends RepresentationModel<DeviceDTO> {
    private UUID id;
    @NotNull
    private String description;

    @NotNull
    private String address;

    @NotNull
    private int maxHrEnCon;

    @NotNull
    private UserReferenceDTO  userReferenceDTO;


    public DeviceDTO() {
    }

    public DeviceDTO(UUID id, String description, String address, int maxHrEnCon, UserReferenceDTO userReferenceDTO){
        this.id = id;
        this.description = description;
        this.address=address;
        this.maxHrEnCon=  maxHrEnCon;
        this.userReferenceDTO = userReferenceDTO;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxHrEnCon() {
        return maxHrEnCon;
    }

    public void setMaxHrEnCon(int maxHrEnCon) {
        this.maxHrEnCon = maxHrEnCon;
    }

    public UserReferenceDTO getUserReferenceDTO() {
        return userReferenceDTO;
    }

    public void setUserReferenceDTO(UserReferenceDTO userReferenceDTO) {
        this.userReferenceDTO = userReferenceDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO that = (DeviceDTO) o;
        return Double.compare(that.maxHrEnCon, maxHrEnCon) == 0 &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maxHrEnCon);
    }
}
