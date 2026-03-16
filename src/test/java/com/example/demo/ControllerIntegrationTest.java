package com.example.demo;

import com.example.demo.auth.AuthRequest;
import com.example.demo.auth.TokenResponse;
import com.example.demo.model.Person;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAddUserLoginAndAddPerson() {
        // Step 1: Add a new user
        User newUser = new User("testuser", "password123", List.of("USER"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> userRequest = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> addUserResponse = restTemplate.postForEntity("/auth/addNewUser", userRequest, String.class);
        assertThat(addUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(addUserResponse.getBody()).isEqualTo("User added successfully!");

        // Step 2: Login to get token
        AuthRequest loginRequest = new AuthRequest("testuser", "password123");
        HttpEntity<AuthRequest> loginHttpEntity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<TokenResponse> loginResponse = restTemplate.postForEntity("/auth/login", loginHttpEntity, TokenResponse.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TokenResponse tokenResponse = loginResponse.getBody();
        assertThat(tokenResponse).isNotNull();
        String token = tokenResponse.getToken();
        assertThat(token).isNotNull().isNotEmpty();

        // Step 3: Add a person using the token
        Person newPerson = new Person("John Doe", 30);
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(token);
        HttpEntity<Person> personRequest = new HttpEntity<>(newPerson, authHeaders);

        ResponseEntity<Person> addPersonResponse = restTemplate.postForEntity("/api/person", personRequest, Person.class);
        assertThat(addPersonResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Person createdPerson = addPersonResponse.getBody();
        assertThat(createdPerson).isNotNull();
        assertThat(createdPerson.getName()).isEqualTo("John Doe");
        assertThat(createdPerson.getAge()).isEqualTo(30);

        // Step 4: Verify the person is in the list
        HttpEntity<Void> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<Person[]> getPersonsResponse = restTemplate.exchange("/api/person", HttpMethod.GET, getRequest, Person[].class);
        assertThat(getPersonsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Person[] persons = getPersonsResponse.getBody();
        assertThat(persons).isNotNull();
        assertThat(persons.length).isGreaterThanOrEqualTo(1);
        boolean personFound = false;
        for (Person p : persons) {
            if ("John Doe".equals(p.getName()) && p.getAge() == 30) {
                personFound = true;
                break;
            }
        }
        assertThat(personFound).isTrue();
    }

    @Test
    void testAddPersonWithInvalidToken() {
        // Attempt to add a person with an invalid token
        Person newPerson = new Person("Jane Doe", 25);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("invalid.jwt.token");
        HttpEntity<Person> request = new HttpEntity<>(newPerson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/person", request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
