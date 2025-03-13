package ro.tuc.ds2020.dtos;

import ro.tuc.ds2020.entities.DeviceMessage;
import ro.tuc.ds2020.entities.UserReference;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class DeviceMessageDTO {


    private UUID id;
    @NotNull
    private String description;
    @NotNull
    private String address;
    private int maxHrEnCon;
    @NotNull
    private UUID userId;
    @NotNull
    private String method;


    public DeviceMessageDTO(UUID id, String description, String address, int maxHrEnCon, UUID  userId, String method){
        this.id = id;
        this.description = description;
        this.address=address;
        this.maxHrEnCon=  maxHrEnCon;
        this.userId = userId;
        this.method = method;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMessageDTO that = (DeviceMessageDTO) o;
        return Double.compare(that.maxHrEnCon, maxHrEnCon) == 0 &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maxHrEnCon, userId, method);
    }


}
