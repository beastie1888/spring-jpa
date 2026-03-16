package com.example.demo;

import com.example.demo.auth.AuthRequest;
import com.example.demo.auth.TokenResponse;
import com.example.demo.auth.jwt.JwtService;
import com.example.demo.auth.jwt.UserInfoService;
import com.example.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private UserInfoService service;


    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserInfoService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.service = userService;
    }


    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome this endpoint is not secure");
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<String> addNewUser(@RequestBody User userInfo) {
        return ResponseEntity.ok(service.addUser(userInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(new User(authRequest.username(), authRequest.password(), List.of()));
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
