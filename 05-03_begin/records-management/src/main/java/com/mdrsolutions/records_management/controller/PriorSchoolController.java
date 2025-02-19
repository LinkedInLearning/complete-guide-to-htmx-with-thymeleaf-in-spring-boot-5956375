package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.controller.dto.PriorSchoolDto;
import com.mdrsolutions.records_management.entity.PriorSchool;
import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.service.CheckStudentMissingFieldService;
import com.mdrsolutions.records_management.service.PriorSchoolService;
import com.mdrsolutions.records_management.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/prior-schools")
public class PriorSchoolController {

    public static final Logger LOGGER = LoggerFactory.getLogger(PriorSchoolController.class);
    private final PriorSchoolService priorSchoolService;
    private final StudentService studentService;
    private final CheckStudentMissingFieldService missingFieldService;

    public PriorSchoolController(PriorSchoolService priorSchoolService, StudentService studentService, CheckStudentMissingFieldService missingFieldService) {
        this.priorSchoolService = priorSchoolService;
        this.studentService = studentService;
        this.missingFieldService = missingFieldService;
    }

    @GetMapping("/student/{studentId}")
    public String showPriorSchools(@PathVariable Long studentId, Model model) {
        LOGGER.info("showPriorSchools");
        List<PriorSchoolDto> priorSchoolDtoList = priorSchoolService.getPriorSchoolDtosByStudentId(studentId);
        model.addAttribute("priorSchoolDtoList", priorSchoolDtoList);
        model.addAttribute("studentId", studentId);
        return "priorSchool/prior-schools :: prior-schools";
    }

    @GetMapping("/student/{studentId}/add")
    public String showAddPriorSchoolForm(@PathVariable Long studentId, Model model) {
        PriorSchoolDto priorSchoolDto = priorSchoolService.initializePriorSchoolDto(studentId);
        model.addAttribute("priorSchoolDto", priorSchoolDto);
        model.addAttribute("studentId", studentId);
        return "priorSchool/add-edit-prior-school :: prior-school-form";
    }

    @GetMapping("/school/{id}/edit")
    public String showEditPriorSchoolForm(@PathVariable Long id, Model model) {
        PriorSchoolDto priorSchoolDto = priorSchoolService.getPriorSchoolDtoById(id);
        model.addAttribute("priorSchoolDto", priorSchoolDto);
        model.addAttribute("studentId", priorSchoolDto.studentId());
        return "priorSchool/add-edit-prior-school :: prior-school-form";
    }

    @PostMapping("/student/{studentId}/save")
    public @ResponseBody String savePriorSchool(@PathVariable Long studentId,
                                  @ModelAttribute PriorSchoolDto priorSchoolDto,
                                  Model model) {
        LOGGER.info("savePriorSchool(...) - studentId: {}", studentId);

        PriorSchoolDto psDto = priorSchoolService.savePriorSchool(priorSchoolDto, studentId);

        Student student = studentService.getStudentById(studentId);

        LOGGER.info("saved prior school information");
        // Refresh the list of prior schools
        List<PriorSchool> priorSchools = priorSchoolService.getPriorSchoolsByStudentId(studentId);
        model.addAttribute("priorSchools", priorSchools);
        model.addAttribute("studentId", studentId);

        return "";
    }

    @PostMapping("/school/{id}/update")
    public String updatePriorSchool(@PathVariable Long id,
                                    @ModelAttribute PriorSchoolDto priorSchoolDto,
                                    Model model) {
        LOGGER.info("updatePriorSchool(...) - priorSchoolId: {}", id);

        Pair<Student, PriorSchoolDto> studentPriorSchoolPair = priorSchoolService.savePriorSchoolWith(priorSchoolDto, priorSchoolDto.studentId());
        List<PriorSchoolDto> priorSchoolDtoList = priorSchoolService.getPriorSchoolDtosByStudentId(priorSchoolDto.studentId());
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(studentPriorSchoolPair.getFirst());

        // Refresh the list
        model.addAttribute("priorSchoolDtoList", priorSchoolDtoList);
        model.addAttribute("studentId", priorSchoolDto.studentId());
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        return "priorSchool/prior-schools :: prior-schools";
    }

    @DeleteMapping("/delete/{id}")
    public String deletePriorSchool(@PathVariable Long id, Model model) {
        PriorSchool priorSchool = priorSchoolService.getPriorSchoolById(id);
        Long studentId = priorSchool.getStudent().getStudentId();

        priorSchoolService.deletePriorSchool(id);

        // Refresh the list
        List<PriorSchool> priorSchools = priorSchoolService.getPriorSchoolsByStudentId(studentId);
        model.addAttribute("priorSchools", priorSchools);
        model.addAttribute("studentId", studentId);

        return "priorSchool/prior-schools :: prior-schools";
    }
}
