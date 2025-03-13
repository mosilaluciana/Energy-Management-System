package ro.tuc.ds2020.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.util.UUID;
@Entity
@Table(name = "device")
public class Device implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2") // uuid2  -> uuid
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  //uuid2 cu uuid, uuid cu org.hibernate.id.UUIDGenerator
    @Column(name="id", columnDefinition = "uuid")
    //@Column(name = "id", columnDefinition = "BINARY(16)") // am adaugat
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maxHrEnCon", nullable = false)
    private int maxHrEnCon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_reference_id",foreignKey = @ForeignKey(name = "FK_DEVICE_USER_REFERENCE"), nullable = false)
    private UserReference userReference;


    public Device() {
    }

    public Device(String description, String address, int maxHrEnCon,UserReference userReference) {
        this.description = description;
        this.address = address;
        this.maxHrEnCon = maxHrEnCon;
        this.userReference = userReference;
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

}
