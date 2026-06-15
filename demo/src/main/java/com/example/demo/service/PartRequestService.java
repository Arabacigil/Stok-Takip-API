package com.example.demo.service;

import com.example.demo.model.Part;
import com.example.demo.model.PartRequest;
import com.example.demo.model.PartResponseDTO;
import com.example.demo.model.RequestStatus;
import com.example.demo.repository.PartRepository;
import com.example.demo.repository.PartRequestRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartRequestService {

    private final PartRequestRepository partRequestRepository;
    private final PartRepository partRepository; // Ana depoya bağlanmak için ekledik

    // Constructor'a PartRepository'yi de dahil ettik
    public PartRequestService(PartRequestRepository partRequestRepository, PartRepository partRepository) {
        this.partRequestRepository = partRequestRepository;
        this.partRepository = partRepository;
    }

    public PartRequest createRequest(PartRequest request) {
        request.setRejectionReason(null);
        return partRequestRepository.save(request);
    }

    public List<PartResponseDTO> getAllRequests() {
        List<PartRequest> requests = partRequestRepository.findAll();
        List<PartResponseDTO> dtoList = new ArrayList<>();

        for (PartRequest r : requests) {
            PartResponseDTO dto = new PartResponseDTO();

            dto.setId(r.getId());
            dto.setQuantity(r.getRequestedQuantity());
            dto.setStatus(r.getStatus());
            dto.setRequesterName(r.getRequesterName());

            if (r.getStatus() == RequestStatus.REJECTED) {
                dto.setRejectionReason(r.getRejectionReason());
            } else {
                dto.setRejectionReason("-");
            }

            if (r.getCreatedAt() != null) {
                dto.setCreatedAt(r.getCreatedAt());
            } else {
                dto.setCreatedAt(LocalDateTime.now());
            }

            if (r.getUpdatedAt() != null) {
                dto.setUpdatedAt(r.getUpdatedAt());
            } else {
                dto.setUpdatedAt(LocalDateTime.now());
            }

            // AKILLI EŞLEŞTİRME: partId'yi kullanarak ana depodan parçanın adını ve kodunu buluyoruz
            Part part = partRepository.findById(r.getPartId()).orElse(null);
            if (part != null) {
                dto.setPartName(part.getPartName());
                dto.setPartCode(part.getPartCode());
                dto.setLocation(part.getLocation());
            } else {
                // Eğer parça depodan silinmişse patlamaması için varsayılan değer
                dto.setPartName("Bilinmeyen Parça");
                dto.setPartCode("-");
                dto.setLocation("-");
            }

            dtoList.add(dto);
        }
        return dtoList;
    }

    public PartRequest approveRequest(Long id) {
        PartRequest request = partRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstek bulunamadı!"));
        request.setStatus(RequestStatus.APPROVED);
        request.setRejectionReason(null);
        request.setUpdatedAt(LocalDateTime.now());
        return partRequestRepository.save(request);
    }

    public PartRequest rejectRequest(Long id, String reason) {
        PartRequest request = partRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İstek bulunamadı!"));
        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setUpdatedAt(LocalDateTime.now());
        return partRequestRepository.save(request);
    }
}
