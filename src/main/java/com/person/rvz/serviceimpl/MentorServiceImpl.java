package com.person.rvz.serviceimpl;

import com.person.rvz.entity.Mentor;
import com.person.rvz.repository.MentorRepository;
import com.person.rvz.repository.PersonRepository;
import com.person.rvz.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class MentorServiceImpl implements MentorService {

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    PersonRepository personRepository;

    @Override
    public Mentor createMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    @Override
    public Page<Mentor> getAllMentors(Pageable pageable) {
        return mentorRepository.findAll(pageable);
    }

    @Override
    public Mentor updateMentor(UUID id, Mentor mentor) {
        Mentor existing = mentorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Mentor not found: " + id));
        existing.setName(mentor.getName());
        existing.setAge(mentor.getAge());
        existing.setUserName(mentor.getUserName());
        return mentorRepository.save(existing);
    }

    @Override
    public void deleteMentor(UUID id) {
        if (!mentorRepository.existsById(id)) {
            throw new NoSuchElementException("Mentor not found: " + id);
        }
        if (personRepository.existsByMentorId(id)) {
            throw new IllegalStateException("Cannot delete mentor " + id + ": still assigned to one or more persons");
        }
        mentorRepository.deleteById(id);
    }
}
