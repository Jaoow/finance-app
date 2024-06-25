package dev.jaoow.financeapp.dto;

import dev.jaoow.financeapp.model.Movement;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountSummaryDTO {
    private String name;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private List<Movement> movements;
}