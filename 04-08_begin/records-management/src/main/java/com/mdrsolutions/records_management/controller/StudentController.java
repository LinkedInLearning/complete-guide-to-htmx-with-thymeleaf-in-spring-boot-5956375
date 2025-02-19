package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class StudentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students/{personId}")
    public String getStudentsByPersonId(@PathVariable final Long personId, Model model){
        LOGGER.info("getStudentsByPersonId(...)");
        List<Student> students = studentService.findStudentsByPersonId(personId);
        model.addAttribute("students", students);
        return "dashboard/students :: students";
    }
}
