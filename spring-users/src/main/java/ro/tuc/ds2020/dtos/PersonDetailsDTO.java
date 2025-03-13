package ro.tuc.ds2020.dtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.Link;
import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;
import ro.tuc.ds2020.entities.Role;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PersonDetailsDTO {

    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @AgeLimit(limit = 18)
    private int age;
    @NotNull
    private String password;
    private List<Role> roles;



    public PersonDetailsDTO() {
    }
    public PersonDetailsDTO(UUID id, String name, String address, int age, String password, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.password = password;
        this.roles = roles;
    }



    public PersonDetailsDTO( String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public PersonDetailsDTO(UUID id, String name, String address, int age) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
    }


    public UUID getId() {return id;}

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDetailsDTO that = (PersonDetailsDTO) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, age);
    }

}
