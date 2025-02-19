package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.entity.Student;
import com.mdrsolutions.records_management.entity.User;
import com.mdrsolutions.records_management.service.CheckPersonMissingFieldService;
import com.mdrsolutions.records_management.service.StudentService;
import com.mdrsolutions.records_management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
    private final UserService userService;
    private final CheckPersonMissingFieldService missingFieldService;
    private final StudentService studentService;

    public DashboardController(UserService userService, CheckPersonMissingFieldService missingFieldService, StudentService studentService) {
        this.userService = userService;
        this.missingFieldService = missingFieldService;
        this.studentService = studentService;
    }

    // If the user is authenticated, redirect them to the dashboard
    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails,
                           HttpServletRequest request,
                           Model model) {
        if (userDetails != null) {
            LOGGER.info("homePage(...)");
            Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());
            boolean isHtmxRequest = request.getHeader("HX-Request") != null;

            if (optionalUser.isPresent()) {
                LOGGER.info("homePage(...) - optionalUser is Present");

                MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(optionalUser.get().getPerson());
                User user = optionalUser.get();

                List<Student> students = studentService.findStudentsByPersonId(user.getPerson().getPersonId());

                model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
                model.addAttribute("missingDetailsList",missingDetailsDto.getMissingFields());
                model.addAttribute("user", user);
                model.addAttribute("personId", user.getPerson().getPersonId());
                model.addAttribute("person", user.getPerson());
                model.addAttribute("students", students);
                model.addAttribute("isHtmxRequest", isHtmxRequest);
                return "dashboard/dashboard";
            } else {
                // Handle the case where the user is not found (optional)
                return "redirect:/login?error";
            }
        }
        return "welcome";  // Load the welcome page for unauthenticated users
    }

    @GetMapping("/dashboard")
    public String getDashboard(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request, Model model) {
        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());
        boolean isHtmxRequest = request.getHeader("HX-Request") != null;
        LOGGER.info("getDashboard(...)");

        if (optionalUser.isPresent()) {
            LOGGER.info("getDashboard(...) - optionalUser is Present");
            MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(optionalUser.get().getPerson());

            User user = optionalUser.get();
            List<Student> students = studentService.findStudentsByPersonId(user.getPerson().getPersonId());

            model.addAttribute("user", user);
            model.addAttribute("personId", user.getPerson().getPersonId());
            model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
            model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields() );
            model.addAttribute("person", user.getPerson());
            model.addAttribute("user", user);
            model.addAttribute("students", students);
            model.addAttribute("isHtmxRequest", isHtmxRequest);
            return "dashboard/dashboard";
        } else {
            // Handle the case where the user is not found (optional)
            return "redirect:/login?error";
        }
    }
}
