package dev.jaoow.financeapp.dto;

import dev.jaoow.financeapp.model.MovementType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovementDTO {

    private Long id;

    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    @NotNull(message = "Type cannot be null")
    private MovementType type;
}