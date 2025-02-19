package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.PersonDto;
import com.mdrsolutions.records_management.service.PersonService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxValue;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxPushUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final PersonService personService;

    public AdminController(PersonService personService) {
        this.personService = personService;
    }

    @HxPushUrl(HtmxValue.TRUE)
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // Load all persons or any subset you need
        List<PersonDto> persons = personService.findAllPersonDtoList();

        model.addAttribute("persons", persons);
        // Return the Thymeleaf template name
        return "admin/dashboard";
    }
}
