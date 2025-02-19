package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.entity.User;
import com.mdrsolutions.records_management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    // If the user is authenticated, redirect them to the dashboard
    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/dashboard";
        }
        return "welcome";  // Load the welcome page for unauthenticated users
    }

    @GetMapping("/dashboard")
    public String getDashboard(@AuthenticationPrincipal UserDetails userDetails,
                               HttpServletRequest request,
                               Model model) {
        Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());
        boolean isHtmxRequest = request.getHeader("HX-Request") != null;

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            model.addAttribute("person", user.getPerson());
            model.addAttribute("personId", user.getPerson().getPersonId());
            model.addAttribute("isHtmxRequest", isHtmxRequest);
            return "dashboard/dashboard";
        } else {
            // Handle the case where the user is not found (optional)
            return "redirect:/login?error";
        }
    }
}
