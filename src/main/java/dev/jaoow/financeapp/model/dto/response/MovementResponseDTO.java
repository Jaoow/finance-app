package dev.jaoow.financeapp.model.dto.response;

import dev.jaoow.financeapp.model.MovementType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovementResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private Long categoryId;
    private Long accountId;
    private MovementType type;
}
