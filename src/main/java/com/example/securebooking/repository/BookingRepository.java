package com.example.securebooking.repository;

import com.example.securebooking.model.Booking;
import com.example.securebooking.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUsername(String username);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.destination = :resource " +
            "AND b.endDate >= :startDate " +
            "AND b.startDate <= :endDate")
    List<Booking> findOverlappingBookings(Resource resource, LocalDate startDate, LocalDate endDate);

    List<Booking> findAll();
}
