package com.example.securebooking.repository;

import com.example.securebooking.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> findByName(String name);

    // Получение только активных ресурсов
    List<Resource> findByActiveTrue();
}
