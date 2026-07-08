package com.person.rvz.service;

import com.person.rvz.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {

      Person createPerson (Person person);

      Page<Person> getAllPersons(Pageable pageable);

}
