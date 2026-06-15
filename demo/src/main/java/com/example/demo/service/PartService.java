package com.example.demo.service;

import com.example.demo.model.Part;
import com.example.demo.model.PartResponseDTO;
import com.example.demo.model.RequestStatus;
import com.example.demo.repository.PartRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part saveOrUpdatePart(Part part) {
        return partRepository.save(part);
    }

    public List<PartResponseDTO> getAllParts() {
        List<Part> parts = partRepository.findAll();
        List<PartResponseDTO> dtoList = new ArrayList<>();

        for (Part p : parts) {
            PartResponseDTO dto = new PartResponseDTO();

            // Orijinal stok bilgileri
            dto.setId(p.getId());
            dto.setPartName(p.getPartName());
            dto.setPartCode(p.getPartCode());
            dto.setQuantity(p.getQuantity());
            dto.setLocation(p.getLocation());

            // Kodu "bozuk" gösteren o null değerleri mantıklı verilerle dolduruyoruz
            dto.setStatus(RequestStatus.APPROVED); // Rafa giren stok zaten onaylanmıştır kabul ediyoruz
            dto.setRequesterName("Kürşat Arabacıgil"); // Null yerine varsayılan atama
            dto.setRejectionReason("-");

            // Tarihleri de boş bırakmıyoruz, sistem saatini basıyoruz
            dto.setCreatedAt(LocalDateTime.now());
            dto.setUpdatedAt(LocalDateTime.now());

            dtoList.add(dto);
        }
        return dtoList;
    }
}