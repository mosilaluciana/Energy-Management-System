package ro.tuc.ds2020.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Person  implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2") // uuid2  -> uuid
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  //uuid2 cu uuid, uuid cu org.hibernate.id.UUIDGenerator
    @Column(name="id", columnDefinition = "uuid")
    //@Column(name = "id", columnDefinition = "BINARY(16)") // am adaugat
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "age", nullable = false)
    private int age;

   @Column (name = "password")
   private String password;

//   @Column(name = "role")
//   private Boolean role;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();


    public Person(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public Person(String name, String address, int age, String password) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.password = password;
    }


}

