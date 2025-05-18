package com.example.securebooking.controller;

import com.example.securebooking.model.Booking;
import com.example.securebooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private BookingRepository bookingRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminPanel() {
        return "admin-panel";
    }

    @GetMapping("/admin/bookings")
    public String getMyBookings(Model model) {

        List<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "admin-bookings";
    }
}
