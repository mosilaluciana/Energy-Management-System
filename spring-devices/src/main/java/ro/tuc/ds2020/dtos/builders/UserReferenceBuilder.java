package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.entities.UserReference;

public class UserReferenceBuilder {

    private UserReferenceBuilder() {
    }

    public static UserReferenceDTO toUserReferenceDTO(UserReference userReference) {

        if(userReference ==  null){
            return null;
        }

        UserReferenceDTO dto =new UserReferenceDTO();
        dto.setId(userReference.getId());
        dto.setUserId(userReference.getUserId());


        return dto;
    }


    public static UserReference toEntity(UserReferenceDTO userReferenceDTO) {

        if (userReferenceDTO == null) {
            return null;
        }

        return new UserReference(userReferenceDTO.getUserId());
    }
}
