package com.person.rvz.serviceimpl;

import com.person.rvz.entity.Student;
import com.person.rvz.repository.StudentRepository;
import com.person.rvz.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Student getStudent(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + id));
    }

    @Override
    public Student updateStudent(UUID id, Student student) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + id));
        existing.setName(student.getName());
        existing.setStudentClass(student.getStudentClass());
        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudent(UUID id) {
        if (!studentRepository.existsById(id)) {
            // already deleted: treat as success so repeated calls are idempotent
            return;
        }
        studentRepository.deleteById(id);
    }
}
