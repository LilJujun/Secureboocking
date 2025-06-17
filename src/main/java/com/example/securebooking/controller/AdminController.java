package com.example.securebooking.controller;

import com.example.securebooking.model.Booking;
import com.example.securebooking.model.Resource;
import com.example.securebooking.repository.BookingRepository;
import com.example.securebooking.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@Controller
public class AdminController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResourceRepository resourceRepository;


    @GetMapping("/admin")
    public String adminPanel() {
        return "admin-panel";
    }

    @GetMapping("/admin/resources")
    public String listResources(Model model) {
        List<Resource> resources = resourceRepository.findAll();
        model.addAttribute("resources", resources);
        return "admin-resources";
    }

    @PostMapping("/admin/resources/toggle/{id}")
    public String toggleResource(@PathVariable Long id) {
        Resource res = resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        res.setActive(!res.isActive());
        resourceRepository.save(res);
        return "redirect:/admin/resources";
    }

    @PostMapping("/admin/resources/delete/{id}")
    public String deleteResource(@PathVariable Long id) {
        resourceRepository.deleteById(id);
        return "redirect:/admin/resources";
    }

    @PostMapping("/admin/resources/new")
    public String createResource(@RequestParam String name,
                                 @RequestParam(required = false) String description) {
        Resource res = new Resource();
        res.setName(name);
        res.setDescription(description);
        res.setActive(true);
        resourceRepository.save(res);
        return "redirect:/admin/resources";
    }

    @GetMapping("/admin/bookings")
    public String getAllBookings(Model model) {

        List<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "admin-bookings";
    }

    @PostMapping("/admin/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingRepository.deleteById(id);
        return "redirect:/admin/bookings";
    }
}
