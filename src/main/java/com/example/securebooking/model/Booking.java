package com.example.securebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String destination;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "ФИО обязательно")
    private String fullName;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Телефон обязателен")
    private String phone;

    @Column(length = 1000)
    private String comment;

    // геттеры и сеттеры
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }
}
