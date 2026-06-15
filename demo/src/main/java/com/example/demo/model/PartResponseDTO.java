package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartResponseDTO {

    private Long id;
    private String partName;
    private String partCode;
    private Integer quantity;
    private String location;

    // İstek onay süreçleri için gereken alanlar
    private RequestStatus status;
    private String requesterName;
    private String rejectionReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2026-06-15 10:05:00")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2026-06-15 10:05:00")
    private LocalDateTime updatedAt;
}