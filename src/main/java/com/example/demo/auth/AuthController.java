package com.example.demo.auth;

import com.example.demo.JwtService;
import com.example.demo.UserInfoService;
import com.example.demo.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User userInfo) {
        return service.addUser(userInfo);
    }

    // Removed the role checks here as they are already managed in SecurityConfig

    @PostMapping("/login")
        public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(new User(authRequest.username(), authRequest.password(), List.of()));
            return "{\"token\" : \"" + token + "\"}";
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
