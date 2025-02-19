package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.controller.dto.PriorSchoolDto;
import com.mdrsolutions.records_management.entity.PriorSchool;
import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.repository.PriorSchoolRepository;
import com.mdrsolutions.records_management.repository.StudentRepository;
import com.mdrsolutions.records_management.util.DateConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PriorSchoolService {

    private final Logger LOGGER = LoggerFactory.getLogger(PriorSchoolService.class);

    private final PriorSchoolRepository priorSchoolRepository;
    private final StudentRepository studentRepository;

    public PriorSchoolService(PriorSchoolRepository priorSchoolRepository, StudentRepository studentRepository) {
        this.priorSchoolRepository = priorSchoolRepository;
        this.studentRepository = studentRepository;
    }

    public PriorSchool findPriorSchoolById(Long priorSchoolId){
        return priorSchoolRepository.findById(priorSchoolId).orElse(null);
    }

    public Optional<PriorSchool> findMostRecentSchool(Long studentId) {

        List<PriorSchool> lastPriorSchool = priorSchoolRepository.findMostRecentPriorSchools(studentId);
        return lastPriorSchool.isEmpty() ? Optional.empty() : Optional.of(lastPriorSchool.getFirst());
    }

    public List<PriorSchoolDto> findPriorSchoolDtoListByStudentId(Long studentId){
        List<PriorSchool> priorSchools = priorSchoolRepository.findByStudentStudentIdOrderByOrderIndexAsc(studentId);

        List<PriorSchoolDto> priorSchoolDtoList = new ArrayList<>();
        for(PriorSchool ps : priorSchools){
            priorSchoolDtoList.add(transform(ps));
        }
        return priorSchoolDtoList;
    }

    public List<PriorSchool> getPriorSchoolsByStudentId(Long studentId) {
        //getting the list by last attended in Descending order
        return priorSchoolRepository.findByStudentStudentIdOrderByDateLastAttendedDesc(studentId);
    }

    public PriorSchoolDto getPriorSchoolDtoById(Long priorSchoolId){
        final PriorSchool priorSchool = getPriorSchoolById(priorSchoolId);
        return transform(priorSchool);
    }

    public PriorSchool getPriorSchoolById(Long id) {
        return priorSchoolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Prior School ID"));
    }

    public void save(PriorSchool ps) {
        priorSchoolRepository.save(ps);
    }

    public PriorSchoolDto savePriorSchool(PriorSchoolDto priorSchoolDto, Long studentId) {
        PriorSchool priorSchool = transform(priorSchoolDto, studentId);
        return transform(priorSchoolRepository.saveAndFlush(priorSchool));
    }

    public Pair<Student,PriorSchoolDto> savePriorSchoolWith(PriorSchoolDto priorSchoolDto, Long studentId){
        LOGGER.info("savePriorSchoolWith(...) - studentId = {}", studentId);

        PriorSchoolDto schoolDto = savePriorSchool(priorSchoolDto, studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Set<PriorSchool> priorSchoolSet = student.getPriorSchools();

        priorSchoolSet.removeIf(ps -> ps.getId().equals(schoolDto.priorSchoolId()));

        student.addPriorSchool(transform(schoolDto, studentId));

        student = studentRepository.saveAndFlush(student);
        return Pair.of(student, schoolDto);
    }

    public void updatePriorSchoolOrder(List<Long> priorSchoolIds, Long studentId) {
        //iterate over collection of prior School Ids
        for (int i = 0; i < priorSchoolIds.size(); i++) {

            //get the id from the collection at that element
            Long psId = priorSchoolIds.get(i);

            //query for a fresh PriorSchool record from database
            PriorSchool ps = this.findPriorSchoolById(psId);

            // will ensure that PriorSchool record is not null AND that it has the studentId
            if (ps != null && ps.getStudent().getStudentId().equals(studentId)) {

                //sets the Order index from the iterator and saves the record
                ps.setOrderIndex(i);
                this.save(ps);
            }
        }
    }

    public void deletePriorSchool(Long id) {
        priorSchoolRepository.deleteById(id);
    }

    public PriorSchoolDto initializePriorSchoolDto(Long studentId) {
        return new PriorSchoolDto(studentId,
                null,
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private PriorSchool transform(PriorSchoolDto priorSchoolDto, Long studentId){

        Optional<Student> student = studentRepository.findById(studentId);
        Student student1 = student.get();

        PriorSchool ps = new PriorSchool(
                priorSchoolDto.orderIndex(),
                priorSchoolDto.schoolName(),
                priorSchoolDto.address(),
                priorSchoolDto.city(),
                priorSchoolDto.state(),
                priorSchoolDto.zip(),
                priorSchoolDto.phoneNumber(),
                priorSchoolDto.administratorFirstName(),
                priorSchoolDto.administratorLastName(),
                priorSchoolDto.gpa(),
                priorSchoolDto.gradeLevel(),
                DateConverter.convert(priorSchoolDto.dateStartedAttending()),
                DateConverter.convert(priorSchoolDto.dateLastAttended()),
                student1
        );
        ps.setId(priorSchoolDto.priorSchoolId());

        return ps;
    }
    private PriorSchoolDto transform(PriorSchool priorSchool){
        return new PriorSchoolDto(
                priorSchool.getStudent().getStudentId(),
                priorSchool.getId(),
                priorSchool.getOrderIndex(),
                priorSchool.getSchoolName(),
                priorSchool.getAddress(),
                priorSchool.getCity(),
                priorSchool.getState(),
                priorSchool.getZip(),
                priorSchool.getPhoneNumber(),
                priorSchool.getAdministratorFirstName(),
                priorSchool.getAdministratorLastName(),
                priorSchool.getGpa(),
                priorSchool.getGradeLevel(),
                DateConverter.convert(priorSchool.getDateStartedAttending()),
                DateConverter.convert(priorSchool.getDateLastAttended())
        );
    }
}
