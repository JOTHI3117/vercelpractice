package com.person.rvz.serviceimpl;

import com.person.rvz.dto.PersonPageCacheEntry;
import com.person.rvz.entity.Login;
import com.person.rvz.entity.Mentor;
import com.person.rvz.entity.Person;
import com.person.rvz.entity.Role;
import com.person.rvz.repository.LoginRepository;
import com.person.rvz.repository.MentorRepository;
import com.person.rvz.repository.PersonRepository;
import com.person.rvz.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);
    private static final String PERSONS_CACHE_PREFIX = "persons:all:page:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    @Autowired
    PersonRepository personRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public Person createPerson(Person person) {
        if (person.getMentor() == null || person.getMentor().getId() == null) {
            throw new IllegalArgumentException("A mentor must be selected for this person");
        }
        Mentor mentor = mentorRepository.findById(person.getMentor().getId())
                .orElseThrow(() -> new NoSuchElementException("Mentor not found: " + person.getMentor().getId()));
        person.setMentor(mentor);
        person.setPersonid(personRepository.findMaxPersonId() + 1);
        Person saved = personRepository.save(person);

        if (!loginRepository.existsByUsername(saved.getName())) {
            Login login = new Login();
            login.setUsername(saved.getName());
            login.setPassword(passwordEncoder.encode("admin"));
            login.setRole(Role.USER);
            loginRepository.save(login);
        }

        evictAllPersonsCache();
        return saved;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Person> getAllPersons(Pageable pageable) {
        log.info("getAllPersons called with page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        String cacheKey = PERSONS_CACHE_PREFIX + pageable.getPageNumber() + ":" + pageable.getPageSize();

        PersonPageCacheEntry cached;
        try {
            cached = (PersonPageCacheEntry) redisTemplate.opsForValue().get(cacheKey);
        } catch (SerializationException e) {
            log.warn("Failed to deserialize cached value for key '{}', evicting and falling back to DB", cacheKey, e);
            redisTemplate.delete(cacheKey);
            cached = null;
        }
        if (cached != null) {
            log.info("Cache hit for key '{}', returning persons from Redis cache", cacheKey);
            return new PageImpl<>(cached.getContent(), pageable, cached.getTotalElements());
        }

        log.info("Cache miss for key '{}', fetching persons from DB", cacheKey);
        Page<Person> persons = personRepository.findAll(pageable);
        redisTemplate.opsForValue().set(cacheKey,
                new PersonPageCacheEntry(persons.getContent(), persons.getTotalElements()), CACHE_TTL);
        return persons;
    }

    @Override
    public void deletePerson(UUID id) {
        if (!personRepository.existsById(id)) {
            throw new NoSuchElementException("Person not found: " + id);
        }
        personRepository.deleteById(id);
        evictAllPersonsCache();
    }

    private void evictAllPersonsCache() {
        var keys = redisTemplate.keys(PERSONS_CACHE_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Evicted {} cached 'get all persons' page(s) after data change", keys.size());
        }
    }
}
