package dev.jaoow.financeapp.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountSummaryDTO {
    private String name;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}