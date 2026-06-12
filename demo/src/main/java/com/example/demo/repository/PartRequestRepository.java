package com.example.demo.repository;

import com.example.demo.model.PartRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRequestRepository extends JpaRepository<PartRequest, Long>{
    // Standart CRUD (Ekle, Sil, Güncelle, Listele) işlemleri için boş bırakıyoruz
}