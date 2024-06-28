package dev.jaoow.financeapp.model.dto.response;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private boolean isStandard;
}
