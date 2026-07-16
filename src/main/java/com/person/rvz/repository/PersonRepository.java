package com.person.rvz.repository;

import com.person.rvz.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person,UUID> {

    @Query("SELECT COALESCE(MAX(p.personid), 0) FROM Person p")
    Integer findMaxPersonId();

    boolean existsByMentorId(UUID mentorId);

}
