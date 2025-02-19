package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.entity.User;
import com.mdrsolutions.records_management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    // Helper method to retrieve current user based on the authenticated user
    private Optional<User> getCurrentUser(UserDetails userDetails) {
        return userService.findByEmail(userDetails.getUsername());
    }

    @GetMapping("/user-info/username-exist")
    @ResponseBody
    public String checkUsernameExistence(@RequestParam String email) {
        if (!isValidEmail(email)) {
            return "<span class='text-danger'>Invalid email format!</span>";
        }

        return userService.findByEmail(email)
                .map(user -> "<span class='text-danger'>Username already taken!</span>")
                .orElse("<span class='text-success'>Username available</span>");
    }

    @GetMapping("/user-info/view")
    public String showUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        return getCurrentUser(userDetails)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "dashboard/user-info :: user-info";
                })
                .orElse("redirect:/login?error");
    }

    // Combined method to handle both username and password editing
    @GetMapping("/user-info/{mode}")
    public String editUserInfo(@PathVariable String mode, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        return getCurrentUser(userDetails)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("mode", mode.equals("edit-password") ? "edit-password" : "edit-username");
                    return "dashboard/modify/user-info-edit :: user-info-edit";
                })
                .orElse("redirect:/login?error");
    }

    @PostMapping("/user-info/update-username")
    public String updateUsername(@RequestParam String email,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        Optional<User> originalUser = getCurrentUser(userDetails);

        if (originalUser.isPresent()) {
            if (!isValidEmail(email)) {
                model.addAttribute("user", originalUser.get());
                model.addAttribute("mode", "edit-username");
                model.addAttribute("errorMessage", "Invalid email format. Please enter a valid email.");
                return "dashboard/dashboard";
            }

            if (userService.findByEmail(email).isPresent()) {
                model.addAttribute("user", originalUser.get());
                model.addAttribute("mode", "edit-username");
                model.addAttribute("errorMessage", "User Email already taken! Please select another!");
                return "dashboard/dashboard";
            }

            // Update email and persist
            User user = originalUser.get();
            user.setEmail(email);
            userService.updateUser(user);
            model.addAttribute("message", "User Email updated! Please login again!");
            return "redirect:/login";
        }

        return "redirect:/login?error";
    }

    @PostMapping("/user-info/update-password")
    public String updatePassword(@RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("user", getCurrentUser(userDetails).orElse(null));
            model.addAttribute("mode", "edit-password");
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "dashboard/dashboard";
        }

        return getCurrentUser(userDetails)
                .map(user -> {
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(password));
                    userService.updateUser(user);
                    return "redirect:/dashboard";
                })
                .orElse("redirect:/login?error");
    }
}
