package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.util.UUID;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMessage implements Serializable{

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String description;
    private String address;
    private int maxHrEnCon;
    private UUID userId;
    private String method;

    public DeviceMessage(String description, String address, int maxHrEnCon, UUID userId, String message) {
        this.description = description;
        this.address = address;
        this.maxHrEnCon = maxHrEnCon;
        this.userId = userId;
        this.method = message;
    }
}
