package com.example.demo.controller;

import com.example.demo.model.Part;
import com.example.demo.model.PartResponseDTO; // Model paketinden DTO'yu import ediyoruz
import com.example.demo.service.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor; // Injection için gerekli
import java.util.List;

@RestController
@RequestMapping("/api/parts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // partService alanını otomatik enjekte eder
@Tag(name = "1. Stok Yönetimi API", description = "Fabrika parça stok girişleri ve stok takibi")
public class PartController {

    private final PartService partService;

    @PostMapping
    @Operation(summary = "Yeni Parça Ekle veya Stok Güncelle")
    public Part addOrUpdatePart(@RequestBody Part part) {
        return partService.saveOrUpdatePart(part);
    }

    @GetMapping
    @Operation(summary = "Mevcut Stok Listesini Getir (DTO- ID'ler Gizli)")
    public List<PartResponseDTO> getAllParts() {
        return partService.getAllParts();
    }
}