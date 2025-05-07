package com.example.securebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboardPage(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "dashboard";
    } 
}

