package dev.jaoow.financeapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private Long id;

    @NotBlank(message = "Account name is required")
    private String name;

    @DecimalMin(value = "0.00", message = "Initial balance must be at least 0.00")
    private BigDecimal initialBalance = BigDecimal.ZERO;
}
