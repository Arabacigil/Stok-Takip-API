package com.example.demo.service;

import com.example.demo.model.Part;
import com.example.demo.model.PartRequest;
import com.example.demo.model.RequestStatus;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.PartRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;
    private final PartRequestRepository requestRepository;

    public PartService(PartRepository partRepository, PartRequestRepository requestRepository) {
        this.partRepository = partRepository;
        this.requestRepository = requestRepository;
    }

    // 1. Stok Girişi ve Güncelleme Metodu
    public Part saveOrUpdatePart(Part part) {
        return partRepository.findByPartCode(part.getPartCode())
                .map(existingPart -> {
                    existingPart.setQuantity(existingPart.getQuantity() + part.getQuantity());
                    return partRepository.save(existingPart);
                })
                .orElseGet(() -> partRepository.save(part));
    }

    // 2. Stok Takibi İçin Tüm Parçaları Getiren Metot (Controller'ın aradığı metot tam olarak bu!)
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    // 3. Parça İsteği Oluşturma Metodu
    public PartRequest createRequest(PartRequest request) {
        partRepository.findById(request.getPartId())
                .orElseThrow(() -> new RuntimeException("Sistemde böyle bir parça kayıtlı değil!"));

        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    // Tüm İstekleri Listeleme Metodu
    public List<PartRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    // 4. İsteği Onaylama Metodu
    @Transactional
    public PartRequest approveRequest(Long requestId) {
        PartRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("İstek bulunamadı!"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Bu istek daha önce zaten sonuçlandırılmış!");
        }

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new RuntimeException("İlgili parça stokta bulunamadı!"));

        if (part.getQuantity() < request.getRequestedQuantity()) {
            throw new RuntimeException("Yetersiz stok! Depodaki mevcut miktar: " + part.getQuantity());
        }

        part.setQuantity(part.getQuantity() - request.getRequestedQuantity());
        partRepository.save(part);

        request.setStatus(RequestStatus.APPROVED);
        return requestRepository.save(request);
    }

    // 5. İsteği Reddetme Metodu
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
