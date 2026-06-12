package com.example.demo.service;

import com.example.demo.model.Part;
import com.example.demo.model.PartResponseDTO;
import com.example.demo.repository.PartRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors; // Bu importun olduğundan emin ol

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public Part saveOrUpdatePart(Part part) {
        return partRepository.findByPartCode(part.getPartCode())
                .map(existingPart -> {
                    existingPart.setQuantity(existingPart.getQuantity() + part.getQuantity());
                    return partRepository.save(existingPart);
                })
                .orElseGet(() -> partRepository.save(part));
    }

    public List<PartResponseDTO> getAllParts() {
        return partRepository.findAll().stream()
                .map(part -> new PartResponseDTO(
                        part.getPartName(),
                        part.getPartCode(),
                        part.getQuantity(),
                        part.getLocation()
                ))
                .collect(Collectors.toList()); // Burada Collector yerine Collectors kullandık
    }
}