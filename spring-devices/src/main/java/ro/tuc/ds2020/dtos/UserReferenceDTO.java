package ro.tuc.ds2020.dtos;


import org.apache.catalina.User;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.UserReference;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class UserReferenceDTO extends RepresentationModel<UserReferenceDTO> {
    private UUID id;
    private UUID userId;

    @NotNull
    private UserReference userReference;

    public UserReferenceDTO() {
    }

    public UserReferenceDTO(UUID userID) {
        this.userId = userID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID userID) {
        this.userId = userID;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userID) {
        this.userId = userID;
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
        UserReferenceDTO that = (UserReferenceDTO) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);}
}
