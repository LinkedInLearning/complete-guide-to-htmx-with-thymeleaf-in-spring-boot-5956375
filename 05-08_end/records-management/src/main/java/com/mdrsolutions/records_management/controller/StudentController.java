package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.controller.dto.PriorSchoolDto;
import com.mdrsolutions.records_management.controller.dto.StudentDto;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.service.CheckStudentMissingFieldService;
import com.mdrsolutions.records_management.service.PriorSchoolService;
import com.mdrsolutions.records_management.service.StudentService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@Controller
public class StudentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;
    private final CheckStudentMissingFieldService missingFieldService; // Service to check for missing fields
    private final PriorSchoolService priorSchoolService;

    public StudentController(StudentService studentService, CheckStudentMissingFieldService missingFieldService, PriorSchoolService priorSchoolService) {
        this.studentService = studentService;
        this.missingFieldService = missingFieldService;
        this.priorSchoolService = priorSchoolService;
    }

    @GetMapping("/students/{personId}/personId")
    public String getStudentsByPersonId(@PathVariable final Long personId, Model model){
        LOGGER.info("getStudentsByPersonId(...) - personId:{}", personId);
        List<StudentDto> studentDtos = studentService.findStudentDtoListByPersonId(personId);

        model.addAttribute("studentDtos", studentDtos);
        return "dashboard/students :: students";
    }

    @HxPushUrl
    @GetMapping("/student/view/{studentId}")
    public String viewStudentFullDetails(@PathVariable("studentId") Long studentId, Model model) {
        LOGGER.info("viewStudentFullDetails(...) - studentId: {}", studentId);
        Student student = studentService.getStudentById(studentId);

        // Initialize guardians collection if using lazy loading
        Set<Person> guardians = student.getGuardians();
        guardians.size(); // Forces initialization

        //retrieving presorted school list by last attended descending order
        List<PriorSchoolDto> sortedPriorSchoolDtoList = priorSchoolService.getPriorSchoolDtosByStudentId(studentId);

        // Check for missing fields
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(student);

        // Add attributes to the model
        model.addAttribute("student", student);
        model.addAttribute("priorSchoolDtoList", sortedPriorSchoolDtoList);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        return "student/student-full-details";
    }

    @HxPushUrl
    @GetMapping("/student/edit/{studentId}")
    public String showEditStudentForm(@PathVariable("studentId") Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId);

        model.addAttribute("student", student);

        return "student/editable-student-form :: student-edit-form";
    }

    @PostMapping("/student/update/{studentId}")
    @HxRequest
    public String updateStudent(@PathVariable("studentId") Long studentId,
                                @ModelAttribute Student student,
                                Model model,
                                HtmxResponse htmxResponse) {
        // Update the student
        LOGGER.info("updateStudent(...) - studentId: {}", studentId);
        Student existingStudent = studentService.getStudentById(studentId);

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setMiddleName(student.getMiddleName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setSuffix(student.getSuffix());
        existingStudent.setSex(student.getSex());
        existingStudent.setDateOfBirth(student.getDateOfBirth());
        // Update other fields as necessary

        studentService.saveStudent(existingStudent);

        // Check for missing fields
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(existingStudent);

        // Add attributes to the model
        model.addAttribute("student", existingStudent);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        htmxResponse.setPushUrl("/student/view/"+ studentId );
        htmxResponse.addTrigger("triggerMarkForReview");

        return "student/student-details-info :: student-details-info";
    }

    @GetMapping("/student/add/{personId}/personId")
    public String showAddStudentForm(@PathVariable("personId") Long personId, Model model){
        LOGGER.info("showAddStudentForm(...) - personId:{}", personId);
        Person person = studentService.findPersonByPersonId(personId);
        Student newStudent = new Student();

        model.addAttribute("student", newStudent);
        model.addAttribute("personId", personId);
        model.addAttribute("person", person);

        return "dashboard/modify/editable-student-form :: student-edit-form";
    }

    @PostMapping("/student/save/{personId}/personId")
    public String saveStudent(@PathVariable("personId") Long personId,
                              @ModelAttribute Student student,
                              Model model){
        LOGGER.info("saveStudent(...) - personId:{}",personId);
        Person person = studentService.findPersonByPersonId(personId);

        student.setGuardians(Set.of(person));
        studentService.saveStudent(student);

        List<StudentDto> studentDtos = studentService.findStudentDtoListByPersonId(personId);

        model.addAttribute("studentDtos", studentDtos);
        return "dashboard/students :: students";
    }

    @GetMapping("/student/checkForReview/{studentId}")
    @HxRequest
    public String getDetailsMarkedForReview(@PathVariable("studentId")Long studentId,
                                            Model model) {
        LOGGER.info("getDetailsMarkedForReview(...) - studentId :{}",studentId);
        Student student = studentService.getStudentById(studentId);
        Set<Person> guardians = student.getGuardians();
        //guardians.size(); // Forces initialization

        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(student);

        model.addAttribute("student", student);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        return "student/mark-for-review :: mark-for-review-info";
    }

}
