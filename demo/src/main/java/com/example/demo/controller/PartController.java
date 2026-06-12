package com.example.demo.controller;

import com.example.demo.model.Part;
import com.example.demo.model.PartRequest;
import com.example.demo.service.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Parça Stok ve İstek Yönetimi", description = "Fabrika parça stok girişleri, personel istekleri ve yönetici onay mekanizması API uç noktaları")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    // --- 1. STOK GİRİŞİ ---
    @PostMapping("/parts")
    @Operation(summary = "Yeni Parça Ekle veya Stok Güncelle",
            description = "Sisteme yeni bir parça koduyla giriş yapar. Eğer parça kodu zaten varsa, girilen miktarı mevcut stoğun üzerine ekler.")
    public Part addOrUpdatePart(@RequestBody Part part) {
        return partService.saveOrUpdatePart(part);
    }

    // --- 2. STOK TAKİBİ ---
    @GetMapping("/parts")
    @Operation(summary = "Mevcut Stok Listesini Getir (Stok Takibi)",
            description = "Depoda kayıtlı olan tüm parçaları, kodlarını, miktarlarını ve raf konumlarını listeler.")
    public List<Part> getAllParts() {
        return partService.getAllParts();
    }

    // --- 3. PARÇA İSTEĞİ YAPMA ---
    @PostMapping("/requests")
    @Operation(summary = "Yeni Parça İsteği Oluştur",
            description = "Personelin ihtiyaç duyduğu parça için talep oluşturmasını sağlar. İstek durumu varsayılan olarak 'PENDING' (Beklemede) kaydedilir.")
    public PartRequest createRequest(@RequestBody PartRequest request) {
        return partService.createRequest(request);
    }

    // --- 4. TÜM İSTEKLERİ LİSTELEME ---
    @GetMapping("/requests")
    @Operation(summary = "Tüm Parça İsteklerini Listele",
            description = "Sistem üzerinden yapılmış olan tüm bekleyen, onaylanan ve reddedilen talepleri durumlarıyla birlikte getirir.")
    public List<PartRequest> getAllRequests() {
        return partService.getAllRequests();
    }

    // --- 5. ONAY VERME ---
    @PutMapping("/requests/{id}/approve")
    @Operation(summary = "Parça İsteğini Onayla",
            description = "Bekleyen bir talebi onaylar. Onaylandığı an, talep edilen miktar ilgili parçanın stoğundan otomatik olarak düşülür. Stok yetersizse hata fırlatır.")
    public PartRequest approveRequest(
            @Parameter(description = "Onaylanacak isteğin benzersiz ID numarası", example = "1")
            @PathVariable Long id) {
        return partService.approveRequest(id);
    }

    // --- 6. RED VERME ---
    @PutMapping("/requests/{id}/reject")
    @Operation(summary = "Parça İsteğini Reddet",
            description = "Bekleyen bir talebi gerekçe göstererek reddeder. Stok miktarında herhangi bir değişiklik yapılmaz.")
    public PartRequest rejectRequest(
            @Parameter(description = "Reddedilecek isteğin benzersiz ID numarası", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Talebin reddedilme nedeni/gerekçesi", example = "Hatalı parça kodu seçimi veya bütçe yetersizliği")
            @RequestParam String reason) {
        return partService.rejectRequest(id, reason);
    }
}