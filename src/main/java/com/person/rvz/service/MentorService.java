package com.person.rvz.service;

import com.person.rvz.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MentorService {

    Mentor createMentor(Mentor mentor);

    Page<Mentor> getAllMentors(Pageable pageable);

    Mentor updateMentor(UUID id, Mentor mentor);

    void deleteMentor(UUID id);

}
