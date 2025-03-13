package ro.tuc.ds2020.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_reference")
public class UserReference implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2") // uuid2  -> uuid
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  //uuid2 cu uuid, uuid cu org.hibernate.id.UUIDGenerator
    //inainte era si
    @Column(name="id", columnDefinition = "uuid")
    //@Column(name = "id", columnDefinition = "BINARY(16)") // am adaugat
    private UUID id;

    //@Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)") // ID of the user from the user database
    private UUID userId;

    public UserReference() {
    }


    public UserReference(UUID userID) {
        this.userId = userID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userID) {
        this.userId = userID;
    }
}
