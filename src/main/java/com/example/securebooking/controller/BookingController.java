// controller/BookingController.java
package com.example.securebooking.controller;

import com.example.securebooking.model.Booking;
import com.example.securebooking.model.User;
import com.example.securebooking.repository.BookingRepository;
import com.example.securebooking.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class BookingController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        return "booking";
    }


    @PostMapping("/booking")
    public String processBooking(@Valid @ModelAttribute Booking booking, BindingResult result,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (result.hasErrors()) {
            return "booking";
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        booking.setUser(user);
        bookingRepository.save(booking);
        return "redirect:/booking-success";
    }


    @GetMapping("/booking-success")
    public String bookingSuccess() {
        return "booking-success";
    }
}
