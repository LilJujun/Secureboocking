// controller/BookingController.java
package com.example.securebooking.controller;

import com.example.securebooking.model.Booking;
import com.example.securebooking.model.User;
import com.example.securebooking.repository.BookingRepository;
import com.example.securebooking.repository.ResourceRepository;
import com.example.securebooking.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("resources", resourceRepository.findByActiveTrue());
        return "booking";
    }

    @GetMapping("/booking/{id}")
    public String getBooking(@PathVariable Long id, Model model, Principal principal) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String username = principal.getName();
        if (!booking.getUser().getUsername().equals(username)) {
            log.info("Пользователь '{}' пытался незаконно посмотреть бронирование id={}", principal.getName(), id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        log.info("Пользователь '{}' просматривает бронирование id={}", principal.getName(), id);
        model.addAttribute("booking", booking);
        return "booking-detail";
    }

    @PostMapping("/booking/delete/{id}")
    public String deleteBooking(@PathVariable Long id, Principal principal) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String username = principal.getName();

        if (!booking.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нельзя удалить чужую бронь");
        }

        bookingRepository.delete(booking);
        return "redirect:/bookings";
    }



    @PostMapping("/booking")
    public String processBooking(@Valid @ModelAttribute Booking booking, BindingResult result,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (result.hasErrors()) {
            return "booking";
        }

        // Проверка: дата начала <= дата окончания
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            result.rejectValue("startDate", null, "Дата начала не может быть позже даты окончания");
            return "booking";
        }

        // Проверка: ресурс уже занят?
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                booking.getDestination(),
                booking.getStartDate(),
                booking.getEndDate()
        );

        if (!overlapping.isEmpty()) {
            result.rejectValue("startDate", null, "Ресурс уже забронирован на выбранные даты");
            return "booking";
        }

        // Связываем с пользователем и сохраняем
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        booking.setUser(user);
        bookingRepository.save(booking);

        log.info("Пользователь '{}' создает бронирование id={}", user.getId(), booking.getId());
        return "redirect:/booking-success";
    }

    @GetMapping("/bookings")
    public String getMyBookings(Model model, Principal principal) {
        String username = principal.getName();
        List<Booking> bookings = bookingRepository.findByUserUsername(username);
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }



    @GetMapping("/booking-success")
    public String bookingSuccess() {
        return "booking-success";
    }
}
