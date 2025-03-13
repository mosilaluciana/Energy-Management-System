package ro.tuc.ds2020.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.services.PersonService;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;
    private final PersonRepository personRepository;


    @Autowired
    public PersonController(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository= personRepository;
    }

    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getPersons() {
        List<PersonDTO> dtos = personService.findPersons();
        for (PersonDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/withId")
    public ResponseEntity<List<Person>> getPerson() {
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }
//
//    @PostMapping()
//    public ResponseEntity<UUID> insertPerson(@RequestBody PersonDetailsDTO personDTO) {
//        UUID personID = personService.insert(personDTO);
//        return new ResponseEntity<>(personID, HttpStatus.CREATED);
//    }

    @PostMapping()
    public ResponseEntity<UUID> insertPerson(@RequestBody PersonDetailsDTO personDTO, HttpServletRequest request) {
        UUID personId = personService.insert(personDTO, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(personId);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("id") UUID personId) {
        PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/person/address")
    public ResponseEntity<PersonDTO> updatePersonByAddress(@RequestBody PersonDTO personDTO) {
        String address = personDTO.getAddress();
        PersonDTO updatedPerson = personService.updatePersonByAddress(address, personDTO);
        return ResponseEntity.ok(updatedPerson);
    }


    @DeleteMapping("/person/address/{address}")
    public ResponseEntity<String> deletePersonByAddress(@PathVariable String address) {
        boolean isDeleted = personService.deletePersonByAddress(address);

        if (isDeleted) {
            return ResponseEntity.ok("Person deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<UUID> getUserIdByEmail(@PathVariable String email) {
        try {
            PersonDetailsDTO user = personService.findPersonByAddress(email);
            return new ResponseEntity<>(user.getId(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


//   LOGIN

//    @PostMapping("/login")
//    public ResponseEntity<String> loggedPerson(@RequestParam("address") String address,
//                                               @RequestParam("password") String password) {
//        PersonDTO personDTO = personService.findPersonByAddressAndPassword(address, password);
//        if (personDTO == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<String>(String.valueOf(personDTO.getRole()), HttpStatus.OK);
//        }
//    }




}
