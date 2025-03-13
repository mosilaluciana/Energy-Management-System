package ro.tuc.ds2020.controllers;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.dtos.AuthResponseDTO;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.repositories.RoleRepository;
import ro.tuc.ds2020.security.JWTGenerator;
import ro.tuc.ds2020.services.PersonService;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestParam("address") String address,
    @RequestParam("password") String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        address,
                        password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }
}
