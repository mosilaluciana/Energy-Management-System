package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.services.UserReferenceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user-reference")
public class UserReferenceController {
    private final UserReferenceService userReferenceService;

    @Autowired
    public UserReferenceController(UserReferenceService userReferenceService) {
        this.userReferenceService = userReferenceService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserReference> getUserReferenceByUserId(@PathVariable UUID userId) {
        try {
            UserReference userReference = userReferenceService.getUserReferenceByUserId(userId);
            return new ResponseEntity<>(userReference, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserReference>> getAllUserReferences() {
        List<UserReference> references = userReferenceService.getAllUserReferences();
        return ResponseEntity.ok(references);
    }
    @PostMapping()
    public ResponseEntity<Void> addUserReference(@Valid @RequestBody UUID userId) {
        userReferenceService.addUserReference(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserReference(@PathVariable UUID id) {
        userReferenceService.deleteReferencesByUserId(id);
        return ResponseEntity.noContent().build();
    }
}
