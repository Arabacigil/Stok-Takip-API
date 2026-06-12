package com.example.demo.controller;

import com.example.demo.model.PartRequest;
import com.example.demo.service.PartRequestService;
import  io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "2. Talep ve Onay Mekanizması API", description = "Personel parça talepleri ve yönetici onay/red süreçleri")
public class PartRequestController {

    private final PartRequestService requestService;

    @PostMapping
    @Operation(summary = "Yeni Parça İsteği Oluştur")
    public PartRequest createRequest(@RequestBody PartRequest request) {
        return requestService.createRequest(request);
    }

    @GetMapping
    @Operation(summary = "Tüm Parça İsteklerini listele")
    public List<PartRequest> getAllRequests() {
        return requestService.getAllRequests();
    }
    @PutMapping("/{id}/approve")
    @Operation(summary = "Parça İsteiğini Onayla (Stoktan Otomatik Düşer)")
    public PartRequest approveRequest(
            @Parameter(description = "Onaylanacak isteğin ID'si", example = "1")
            @PathVariable Long id){
        return requestService.approveRequest(id);
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Parça İsteğini Reddet")
    public PartRequest rejectRequest(
            @Parameter(description = "Reddedilecek isteğin ID'si", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Red nedeni" , example = "Hatalı kod")
            @RequestParam String reason) {
        return requestService.rejectRequest(id, reason);
    }
}