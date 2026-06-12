package com.example.demo.service;

import com.example.demo.model.Part;
import com.example.demo.model.PartResponseDTO;
import com.example.demo.repository.PartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartService partService;

    private Part samplePart;

    @BeforeEach
    void setUp() {
        samplePart = new Part(1L, "Rulman", "RLM-101", 100, "A-3 Rafi");
    }

    @Test
    void saveOrUpdatePart_WhenPartIsNew_ShouldSaveSuccessfully() {
        when(partRepository.findByPartCode("RLM-101")).thenReturn(Optional.empty());
        when(partRepository.save(any(Part.class))).thenReturn(samplePart);

        Part savedPart = partService.saveOrUpdatePart(samplePart);

        assertNotNull(savedPart);
        assertEquals("RLM-101", savedPart.getPartCode());
        verify(partRepository, times(1)).save(samplePart);
    }

    @Test
    void getAllParts_ShouldReturnDtoList() {
        when(partRepository.findAll()).thenReturn(List.of(samplePart));

        List<PartResponseDTO> dtoList = partService.getAllParts();

        assertFalse(dtoList.isEmpty());
        assertEquals("RLM-101", dtoList.get(0).getPartCode());
    }
}
