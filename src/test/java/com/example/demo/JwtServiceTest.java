package com.example.demo;

import com.example.demo.auth.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "security.jwt.secret-key=5367566859703373367639792F423F452848284D6251655468576D5A71347437",
    "security.jwt.expiration-time=3600000"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenValidWithWrongUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails wrongUser = new User("wronguser", "password", Collections.emptyList());
        assertFalse(jwtService.isTokenValid(token, wrongUser));
    }

    @Test
    void testExtractClaim() {
        String token = jwtService.generateToken(userDetails);
        String subject = jwtService.extractClaim(token, claims -> claims.getSubject());
        assertEquals("testuser", subject);
    }

    @Test
    void testGetExpirationTime() {
        long expirationTime = jwtService.getExpirationTime();
        assertEquals(3600000, expirationTime);
    }
}
