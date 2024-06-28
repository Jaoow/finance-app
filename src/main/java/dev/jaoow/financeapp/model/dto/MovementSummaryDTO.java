package dev.jaoow.financeapp.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovementSummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
}
