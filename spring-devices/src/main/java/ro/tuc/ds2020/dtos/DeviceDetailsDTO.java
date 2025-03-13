package ro.tuc.ds2020.dtos;

import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;
import ro.tuc.ds2020.entities.UserReference;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {

    private UUID id;
    @NotNull
    private String description;

    @NotNull
    private String address;

    @NotNull
    private int maxHrEnCon;

    @NotNull
    private UserReference userReference;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(String description, String address, int maxHrEnCon, UserReference userReference) {
        this.description = description;
        this.address = address;
        this.maxHrEnCon = maxHrEnCon;
        this.userReference = userReference;
    }

    public DeviceDetailsDTO(UUID id, String description, String address, int maxHrEnCon,UserReference userReference) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxHrEnCon = maxHrEnCon;
        this.userReference =userReference;
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

    public UserReference getUserReference() {
        return userReference;
    }

    public void setUserReference(UserReference userReference) {
        this.userReference = userReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return maxHrEnCon == that.maxHrEnCon &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maxHrEnCon);
    }
}
