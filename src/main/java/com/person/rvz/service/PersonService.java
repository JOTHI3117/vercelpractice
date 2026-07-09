package com.person.rvz.service;

import com.person.rvz.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PersonService {

      Person createPerson (Person person);

      Page<Person> getAllPersons(Pageable pageable);

      void deletePerson(UUID id);

}
