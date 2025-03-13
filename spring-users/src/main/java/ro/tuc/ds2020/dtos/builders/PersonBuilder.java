package ro.tuc.ds2020.dtos.builders;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getId(), person.getName(), person.getAddress(), person.getAge(),person.getPassword());
    }

    public static PersonDetailsDTO toPersonDetailsDTO(Person person) {
        return new PersonDetailsDTO(person.getId(), person.getName(), person.getAddress(), person.getAge(),person.getPassword(), person.getRoles());
    }

    public static Person toEntity(PersonDetailsDTO personDetailsDTO) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        // Encrypt the password using BCryptPasswordEncoder
        String hashedPassword = passwordEncoder.encode(personDetailsDTO.getPassword());

        return new Person(personDetailsDTO.getName(),
                personDetailsDTO.getAddress(),
                personDetailsDTO.getAge(),
                hashedPassword);
    }
}
