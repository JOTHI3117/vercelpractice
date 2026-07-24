package com.person.rvz.service;

import com.person.rvz.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentService {

    Student createStudent(Student student);

    Page<Student> getAllStudents(Pageable pageable);

    Student getStudent(UUID id);

    // idempotent: PUT replaces the full resource state; same request body
    // always leaves the student in the same end state, no matter how many
    // times it is called.
    Student updateStudent(UUID id, Student student);

    // idempotent: deleting an already-deleted (or never-existing) student
    // is a no-op rather than an error, so retries are safe.
    void deleteStudent(UUID id);
}
