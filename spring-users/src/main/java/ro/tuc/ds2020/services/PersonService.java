package ro.tuc.ds2020.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.DuplicateResourceException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.entities.Role;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.repositories.RoleRepository;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }


    public PersonDetailsDTO findPersonByAddress(String email) {
        Optional<Person> personOptional = personRepository.findByAddress(email);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with email {} was not found in db", email);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + email);
        }
        return PersonBuilder.toPersonDetailsDTO(personOptional.get());
    }



    public PersonDetailsDTO findPersonById(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }
//
//    public UUID insert(PersonDetailsDTO personDTO) {
//
//        personRepository.findByAddress(personDTO.getAddress())
//                .ifPresent(existingPerson -> {
//                    throw new DuplicateResourceException("The address already exists!");
//                });
//
//        Person person = PersonBuilder.toEntity(personDTO);
//        Role userRole = roleRepository.findByName(personDTO.getRoles().get(0).getName())
//                .orElseThrow(() -> new RuntimeException("Role 'USER' not found"));
//        person.setRoles(Collections.singletonList(userRole));
//        person = personRepository.save(person);
//
//        System.out.println(person.getId());
//        //LOGGER.debug("personID", person.getId());
//
//        createPersonReference(person.getId());
//
//        LOGGER.debug("Person with id {} was inserted in db", person.getId());
//        return person.getId();
//    }

    public UUID insert(PersonDetailsDTO personDTO, HttpServletRequest request) {
        // Extract the JWT from the Authorization header
        String token = extractJwt(request);
        if (token == null || !validateJwt(token)) {
            throw new SecurityException("Invalid or expired JWT");
        }

        // Check if the address already exists
        personRepository.findByAddress(personDTO.getAddress())
                .ifPresent(existingPerson -> {
                    throw new DuplicateResourceException("The address already exists!");
                });

        // Convert DTO to entity
        Person person = PersonBuilder.toEntity(personDTO);

        // Find the user role and assign it to the person
        Role userRole = roleRepository.findByName(personDTO.getRoles().get(0).getName())
                .orElseThrow(() -> new RuntimeException("Role 'USER' not found"));
        person.setRoles(Collections.singletonList(userRole));

        // Save the person to the database
        person = personRepository.save(person);

        // Log the created person's ID
        LOGGER.debug("Person with id {} was inserted in db", person.getId());

        // Create a reference for the person, using the JWT token
        createPersonReference(person.getId(), token);

        return person.getId();
    }



    public PersonDTO updatePersonByAddress(String address, PersonDTO personDTO) {
        Person person = personRepository.findByAddress(address)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with email: " + address));

        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());

        personRepository.save(person);
        return PersonBuilder.toPersonDTO(person);
    }



    public boolean deletePersonByAddress(String address) {
        Optional<Person> personOpt = personRepository.findByAddress(address);
        if (personOpt.isPresent()) {
            personRepository.delete(personOpt.get());
            return true;
        }
        return false;
    }


    public PersonDTO findPersonByAddressAndPassword(String address, String password) {
        Optional<Person> personOptional = personRepository.findByAddress(address);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with address {} was not found in db", address);
            throw new ResourceNotFoundException("Person with email: " + address + " not found");
        }

        Person person = personOptional.get();
        if (!new BCryptPasswordEncoder(10).matches(password, person.getPassword())) {
            System.out.println(password + "    " + person.getPassword());
            LOGGER.error("Invalid password for user {}", address);
            throw new ResourceNotFoundException("Invalid password");
        }

        return PersonBuilder.toPersonDTO(person);
    }

    //Conexiune intre bazele de date

//    private void createPersonReference(UUID personId) {
//        // Assuming you have an endpoint in the device microservice to create PersonReference
//
//        //String url = "http://tomcat-db-api2:8081/user-reference"; // Adjust URL accordingly
//        String url = "http://localhost:8081/user-reference"; // Adjust URL accordingly
//
//        // Sending a POST request with the personId
//        ResponseEntity<Void> response = restTemplate.postForEntity(url, personId, Void.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            LOGGER.info("PersonReference created for personId: {}", personId);
//        } else {
//            LOGGER.error("Failed to create PersonReference for personId: {}. Response: {}", personId, response.getStatusCode());
//            throw new ResourceNotFoundException("Failed to create PersonReference for personId: " + personId);
//        }
//    }



    private String extractJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remove the "Bearer " prefix
        }
        return null;
    }
    private static final String SECRET_KEY = "your-very-secure-and-longer-key-that-is-at-least-64-characters-long";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private boolean validateJwt(String token) {
        try {
            // Validate the JWT (you can use your existing logic)
            Jwts.parser()
                    .setSigningKey(key)  // Replace with actual signing key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createPersonReference(UUID personId, String token) {
        //String url = "http://localhost:8081/user-reference";
        String url = "http://tomcat-db-api2:8081/user-reference"; // Adjust URL accordingly


        // Create headers and set the Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<UUID> entity = new HttpEntity<>(personId, headers);

        // Send the request to create a reference with the JWT token
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("PersonReference created for personId: {}", personId);
        } else {
            LOGGER.error("Failed to create PersonReference for personId: {}. Response: {}", personId, response.getStatusCode());
            throw new ResourceNotFoundException("Failed to create PersonReference for personId: " + personId);
        }
    }

}
