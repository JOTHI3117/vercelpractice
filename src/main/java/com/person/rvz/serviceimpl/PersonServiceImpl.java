package com.person.rvz.serviceimpl;

import com.person.rvz.entity.Login;
import com.person.rvz.entity.Person;
import com.person.rvz.entity.Role;
import com.person.rvz.repository.LoginRepository;
import com.person.rvz.repository.PersonRepository;
import com.person.rvz.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Person createPerson(Person person) {
        person.setPersonid(personRepository.findMaxPersonId() + 1);
        Person saved = personRepository.save(person);

        if (!loginRepository.existsByUsername(saved.getName())) {
            Login login = new Login();
            login.setUsername(saved.getName());
            login.setPassword(passwordEncoder.encode("admin"));
            login.setRole(Role.USER);
            loginRepository.save(login);
        }

        return saved;
    }

    @Override
    public Page<Person> getAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public void deletePerson(UUID id) {
        if (!personRepository.existsById(id)) {
            throw new NoSuchElementException("Person not found: " + id);
        }
        personRepository.deleteById(id);
    }
}
