package com.example.demo.auth.jwt;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    @Transactional
    public Person updatePerson(Person person) {
        Optional<Person> personOptional = repository.findByName(person.getName());
        if (personOptional.isEmpty()) {
            throw new RuntimeException("Person not found with name: " + person.getName());
        }

        Person personFound = personOptional.get();
        personFound.setAge(person.getAge());

        return repository.save(personFound);
    }

    @Transactional
    public Person createPerson(Person person) {
        return repository.save(person);
    }
}
