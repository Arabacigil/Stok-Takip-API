package com.example.demo.repository;

import com.example.demo.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Long> {
    Optional<Part> findByPartCode(String partCode);
}
