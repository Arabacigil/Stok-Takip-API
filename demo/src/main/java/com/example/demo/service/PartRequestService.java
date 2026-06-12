package com.example.demo.service;

import com.example.demo.model.Part;
import com.example.demo.model.PartRequest;
import com.example.demo.model.RequestStatus;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.PartRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor // Constructor'ı otomatik üreterek Dependency Injection sağlar
public class PartRequestService {

    private final PartRequestRepository requestRepository;
    private final PartRepository partRepository;

    // Personelin parça isteği oluşturmasını sağlar
    public PartRequest createRequest(PartRequest request) {
        partRepository.findById(request.getPartId())
                .orElseThrow(() -> new RuntimeException("Sistemde böyle bir parça kayıtlı değil!"));

        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    // Yapılan tüm istekleri listeler
    public List<PartRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    // İsteği onaylar ve otomatik olarak stoktan düşer
    @Transactional
    public PartRequest approveRequest(Long requestId) {
        PartRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("İstek bulunamadı!"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Bu istek daha önce zaten sonuçlandırılmış!");
        }

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new RuntimeException("İlgili parça stokta bulunamadı!"));

        // Stok yeterli mi kontrolü
        if (part.getQuantity() < request.getRequestedQuantity()) {
            throw new RuntimeException("Yetersiz stok! Depodaki mevcut miktar: " + part.getQuantity());
        }

        // Stoktan düşme işlemi
        part.setQuantity(part.getQuantity() - request.getRequestedQuantity());
        partRepository.save(part);

        // İsteği onaylandıya çekme
        request.setStatus(RequestStatus.APPROVED);
        return requestRepository.save(request);
    }

    // İsteği gerekçe ile reddeder
    public PartRequest rejectRequest(Long requestId, String reason) {
        PartRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("İstek bulunamadı!"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Bu istek daha önce zaten sonuçlandırılmış!");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        return requestRepository.save(request);
    }
}