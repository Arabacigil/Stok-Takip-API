package com.example.demo.controller;

import com.example.demo.model.PartRequest;
import com.example.demo.model.PartResponseDTO;
import com.example.demo.service.PartRequestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/part-requests")
public class PartRequestController {

    private final PartRequestService partRequestService;

    public PartRequestController(PartRequestService partRequestService) {
        this.partRequestService = partRequestService;
    }

    @PostMapping
    @Operation(summary = "Yeni Parça İsteği Oluştur")
    public PartRequest createRequest(@RequestBody PartRequest request) {
        return partRequestService.createRequest(request);
    }

    @GetMapping
    @Operation(summary = "Tüm Parça İsteklerini Listele (İsimler ve Statüler Dahil)")
    public List<PartResponseDTO> getAllRequests() {
        return partRequestService.getAllRequests();
    }

    // Swagger'a geri gelen onaylama butonu
    @PutMapping("/{id}/approve")
    @Operation(summary = "İsteği Onayla (APPROVED yap)")
    public PartRequest approveRequest(@PathVariable Long id) {
        return partRequestService.approveRequest(id);
    }

    // Swagger'a geri gelen reddetme butonu
    @PutMapping("/{id}/reject")
    @Operation(summary = "İsteği Reddet (REJECTED yap)")
    public PartRequest rejectRequest(@PathVariable Long id, @RequestParam String reason) {
        return partRequestService.rejectRequest(id, reason);
    }
}
