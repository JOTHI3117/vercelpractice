package com.person.rvz.serviceimpl;

import com.person.rvz.entity.Person;
import com.person.rvz.repository.PersonRepository;
import com.person.rvz.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }
}
