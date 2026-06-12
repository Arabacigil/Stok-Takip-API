package com.example.demo.repository;

import com.example.demo.model.PartRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRequestRepository extends JpaRepository<PartRequest, Long> {
    // Burası boş kalacak, Spring Data JPA CRUD işlemlerini otomatik yapacak.
}