package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartResponseDTO {
    private String partname;
    private String partCode;
    private Integer quantity;
    private String location;
}