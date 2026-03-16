package com.example.demo;


import com.example.demo.auth.jwt.PersonService;
import com.example.demo.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {


    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(service.getAllPersons());
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        return ResponseEntity.ok(service.updatePerson(person));
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return ResponseEntity.ok(service.createPerson(person));
    }
}
