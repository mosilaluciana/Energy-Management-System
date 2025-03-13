package ro.tuc.ds2020.security;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.SignatureException;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JWTGenerator {
    //private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // Symmetric key (HS512)

    private static final String SECRET_KEY = "your-very-secure-and-longer-key-that-is-at-least-64-characters-long";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Include roles as a claim
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return token;
    }

    // Extract username from JWT token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate JWT tokenA
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT token is expired", ex);
        } catch (MalformedJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT token is malformed", ex);
        } catch (UnsupportedJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT token is unsupported", ex);
        } catch (SignatureException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT token signature is invalid", ex);
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT token is invalid", ex);
        }
    }
}
