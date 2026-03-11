package com.example.demo;


import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class DemoController {


    private final PersonRepository repository;

    public DemoController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Transactional(isolation= Isolation.REPEATABLE_READ)
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return repository.save(person);
    }
}
