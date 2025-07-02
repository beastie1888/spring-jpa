package com.example.demo;


import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
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
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return repository.save(person);
    }
}
