package ro.tuc.ds2020;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.entities.Role;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@SpringBootApplication
@Validated
public class Ds2020Application extends SpringBootServletInitializer {

    private final PersonRepository personRepository;

    public Ds2020Application(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ds2020Application.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Ds2020Application.class, args);
    }

//    // Initializing the admin user upon startup
//    @PostConstruct
//    public void initializeAdminUser() {
//        if (personRepository.findByRole(true).isEmpty()) {
//            Person admin = new Person();
//            admin.setName("admin");
//            admin.setAddress("admin@example.com");
//            admin.setAge(30);
//            String hashedPassword = passwordEncoder().encode("123456");
//
//            admin.setPassword(hashedPassword);
//            admin.setRole(true); // true for admin
//
//            personRepository.save(admin);
//            System.out.println("Admin user created.");
//        }
//    }
@Bean
CommandLineRunner initializeRolesAndAdmin(
        PersonRepository personRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder) { // Add PasswordEncoder here
    return args -> {
        // Ensure ADMIN role exists
        Role adminRole = roleRepository.findById(1).orElseGet(() -> {
            Role role = new Role();
            role.setId(1); // Set specific ID
            role.setName("ADMIN");
            roleRepository.save(role);
            System.out.println("Admin role created.");
            return role;
        });

        // Ensure USER role exists
        Role userRole = roleRepository.findById(2).orElseGet(() -> {
            Role role = new Role();
            role.setId(2); // Set specific ID
            role.setName("USER");
            roleRepository.save(role);
            System.out.println("User role created.");
            return role;
        });

        // Ensure the admin user exists
        Optional<Person> adminUserOpt = personRepository.findByAddress("admin@gmail.com");
        if (!adminUserOpt.isPresent()) {
            Person adminUser = new Person(
                    "Admin User",
                    "admin@gmail.com",
                    30,
                    passwordEncoder.encode("123456") // Hash the password
            );

            // Assign roles to the admin user
            List<Role> roles = new ArrayList<>();
            roles.add(adminRole); // Assign ADMIN role
            adminUser.setRoles(roles);

            personRepository.save(adminUser);
            System.out.println("Admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }
    };
}
}
