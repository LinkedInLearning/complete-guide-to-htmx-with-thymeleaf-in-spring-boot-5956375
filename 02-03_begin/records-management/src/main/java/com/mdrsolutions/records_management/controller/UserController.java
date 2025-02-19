package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.entity.User;
import com.mdrsolutions.records_management.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user-info/edit")
    public String showEditUserForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Get the current user's email based on their authentication
        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user); // Add the user object to the model
            return "dashboard/dashboard"; // Redirect to dashboard with user info
        } else {
            return "redirect:/login?error"; // Redirect to login if user is not found
        }
    }

    @GetMapping("/user-info/edit-username")
    public String editUsername(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            model.addAttribute("mode", "edit-username"); // Set the mode to 'edit-username'
            return "dashboard/dashboard"; // Render dashboard with edit mode
        }
        return "redirect:/login?error"; // Handle case where user is not found
    }

    @GetMapping("/user-info/edit-password")
    public String editPassword(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            model.addAttribute("mode", "edit-password"); // Set the mode to 'edit-password'
            return "dashboard/dashboard"; // Render dashboard with edit mode
        }
        return "redirect:/login?error"; // Handle case where user is not found
    }

    @PostMapping("/user-info/update-username")
    public String updateUsername(@RequestParam String email,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        // Logic for updating the username
        final String originalUserEmail = userDetails.getUsername();
        final Optional<User> originalUser = userService.findByEmail(originalUserEmail);

        // Search to see if new user already exists
        final Optional<User> newUserEmailExits = userService.findByEmail(email);
        if (newUserEmailExits.isPresent()) {
            model.addAttribute("user", originalUser.get());
            model.addAttribute("mode", "edit-username");
            model.addAttribute("errorMessage", "User Email already taken! Please select another!");
            return "dashboard/dashboard";  // Reload the dashboard with an error message
        }

        if (originalUser.isPresent()) {
            // Update originalUser object with new username and persist back to db
            final User user = originalUser.get();
            user.setEmail(email);
            userService.updateUser(user);
            model.addAttribute("mode", "none");
            model.addAttribute("message", "User Email updated! Please login again!");
            return "redirect:/login"; // Redirect to login after successful update
        }

        return "redirect:/login?error"; // Handle case where user is not found
    }

    @PostMapping("/user-info/update-password")
    public String updateUserInfo(@RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("user", userService.findByEmail(userDetails.getUsername()).get());
            model.addAttribute("mode", "edit-password");
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "dashboard/dashboard";  // Reload the dashboard with an error message
        }

        // Find the user by email or username
        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));  // Save the new password securely

            userService.updateUser(user);  // Update the user
            return "redirect:/dashboard";  // Redirect to dashboard after successful update
        } else {
            return "redirect:/login?error";
        }
    }

}
