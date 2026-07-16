package com.person.rvz.repository;

import com.person.rvz.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MentorRepository extends JpaRepository<Mentor, UUID> {
}
