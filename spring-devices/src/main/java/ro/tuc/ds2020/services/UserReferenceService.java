package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.dtos.builders.UserReferenceBuilder;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserReferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReferenceService.class);
    private final UserReferenceRepository userReferenceRepository;

    @Autowired
    public UserReferenceService(UserReferenceRepository userReferenceRepository) {
        this.userReferenceRepository = userReferenceRepository;
    }


    public UserReference getUserReferenceByUserId(UUID userId) {
        Optional<UserReference> reference = userReferenceRepository.findByUserId(userId);
        if (!reference.isPresent()) {
            LOGGER.error("UserReference with id {} was not found", userId);
            throw new ResourceNotFoundException("UserReference with id: " + userId + " not found");
        }
        return reference.get();
    }


    public List<UserReference> getAllUserReferences() {
        List<UserReference> references = userReferenceRepository.findAll();
        LOGGER.info("Retrieved {} user references", references.size());
        return references;
    }


    public UUID addUserReference(UUID userId) {
        UserReference userReference = new UserReference(userId);
       // userReference.setUserId(userId); // Ensure this is set correctly
        userReference = userReferenceRepository.save(userReference);
        LOGGER.info("UserReference for userId {} was added with ID: {}", userId, userReference.getUserId());
        return userReference.getUserId();
    }


    public void deleteReferencesByUserId(UUID userId) {
        List<UserReference> references = userReferenceRepository.findAllByUserId(userId);
        for (UserReference reference : references) {
            userReferenceRepository.delete(reference);
            LOGGER.info("Deleted user reference for userId: {}", userId);
        }
    }
}

    
    
    
    
    
    
