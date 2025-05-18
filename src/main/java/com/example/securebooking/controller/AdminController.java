package com.example.securebooking.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminPanel() {
        return "admin-panel";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/bookings")
    public String adminBookings() {
        return "admin-bookings";
    }
}
