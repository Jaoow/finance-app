package dev.jaoow.financeapp.model.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponseDTO {
    private Long id;
    private String name;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
}
