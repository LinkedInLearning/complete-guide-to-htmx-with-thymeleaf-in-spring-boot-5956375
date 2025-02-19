package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.controller.dto.StudentDto;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.entity.PriorSchool;
import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CheckStudentMissingFieldService checkStudentMissingFieldService;
    private final PriorSchoolService priorSchoolService;
    private final PersonRepository personRepository;

    public StudentService(StudentRepository studentRepository,
                          CheckStudentMissingFieldService checkStudentMissingFieldService, PriorSchoolService priorSchoolService, PersonRepository personRepository) {
        this.studentRepository = studentRepository;
        this.checkStudentMissingFieldService = checkStudentMissingFieldService;
        this.priorSchoolService = priorSchoolService;
        this.personRepository = personRepository;
    }

    public List<Student> findStudentsByPersonId(final Long personId) {
        return studentRepository.findAllByGuardians_PersonId(personId);
    }

    public List<StudentDto> findStudentDtoListByPersonId(final Long personId){
        // Fetch the list of students associated with the given personId
        List<Student> students = findStudentsByPersonId(personId);

        // Iterate over the students list and convert each Student into a StudentDto
        List<StudentDto> studentDtos = students.stream()
                .map(student -> {
                    // Use checkStudentMissingFieldService to get MissingDetailsDto for each student
                    MissingDetailsDto missingDetailsDto = checkStudentMissingFieldService.checkForMissingFields(student);
                    Optional<PriorSchool> mostRecentSchool = priorSchoolService.findMostRecentSchool(student.getStudentId());
                    // Create a new StudentDto for each student

                    return new StudentDto(
                            student.getStudentId(),                   // studentId
                            personId,       // personId (assuming getPerson() is not null)
                            student.getFirstName(),                   // firstName
                            student.getMiddleName(),                  // middleName
                            student.getLastName(),                    // lastName
                            student.getSex(),
                            calculateAge(student.getDateOfBirth(), LocalDate.now()),
                            (mostRecentSchool.isEmpty())? "?" : mostRecentSchool.get().getGradeLevel(),
                            missingDetailsDto                         // missingDetailsDto
                    );
                })
                .collect(Collectors.toList());

        // Return the list of StudentDto objects
        return studentDtos;
    }

    public Student getStudentById(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        return optionalStudent.orElse(null);
    }

    public Student saveStudent(Student existingStudent) {
        return studentRepository.save(existingStudent);
    }

    private String calculateAge(LocalDate dateOfBirth, LocalDate now){
        if(null != dateOfBirth ) {
            Period agePeriod = Period.between(dateOfBirth, now);
            return agePeriod.getYears() + "";
        } else {
            return "";
        }
    }

    public Person findPersonByPersonId(Long personId) {
        return personRepository.findById(personId).get();
    }

    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId).get();
    }
}
