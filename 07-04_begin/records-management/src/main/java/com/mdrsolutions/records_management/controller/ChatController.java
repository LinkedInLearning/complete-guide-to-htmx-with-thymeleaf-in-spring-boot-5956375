package com.mdrsolutions.records_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/ws")
    public String webSocketPage(Model model){
        return "ws/chat";
    }
}
