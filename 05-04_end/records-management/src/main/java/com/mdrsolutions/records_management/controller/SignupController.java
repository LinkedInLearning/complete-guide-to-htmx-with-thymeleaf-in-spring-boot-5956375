package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.RegistrationResponse;
import com.mdrsolutions.records_management.controller.dto.UserDto;
import com.mdrsolutions.records_management.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SignupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignupController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SignupController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/new-user")
    public String showNewUserForm(Model model) {
        UserDto userDto = new UserDto("", "", "", "", "", "", false);
        model.addAttribute("userDto",userDto );
        return "signup/user-registration"; // Redirect to dashboard with user inf
    }


    @PostMapping("/register")
    public String addNewUserForm(@Valid UserDto userDto, BindingResult result, Model model) {

        LOGGER.info(userDto.toString());
        List<ObjectError> allErrors = new ArrayList<>(result.getAllErrors());
        if (result.hasErrors()) {
            model.addAttribute("validationErrors", allErrors);
            model.addAttribute("userDto", userDto);
            return "signup/user-registration";
        }

        RegistrationResponse response = userService.registerUser(userDto);

        if (!response.isSuccess()) {
            allErrors.addAll(response.getErrors());
            model.addAttribute("validationErrors", allErrors);
            model.addAttribute("userDTO", userDto);
            return "signup/user-registration";
        }

        model.addAttribute("message", response.getMessage());
        return "redirect:/login";
    }

}
