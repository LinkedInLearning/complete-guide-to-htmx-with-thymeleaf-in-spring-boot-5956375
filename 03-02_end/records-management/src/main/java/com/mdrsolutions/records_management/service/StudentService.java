package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findStudentsByPersonId(final Long personId) {
        return studentRepository.findAllByGuardians_PersonId(personId);
    }
}
