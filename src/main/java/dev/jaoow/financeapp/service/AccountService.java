package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.dto.AccountDTO;
import dev.jaoow.financeapp.dto.AccountSummaryDTO;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.Account;
import dev.jaoow.financeapp.model.Movement;
import dev.jaoow.financeapp.model.MovementType;
import dev.jaoow.financeapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account createAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setInitialBalance(accountDTO.getInitialBalance());
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, AccountDTO accountDTO) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setName(accountDTO.getName());
        account.setInitialBalance(accountDTO.getInitialBalance());
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public AccountSummaryDTO getAccountSummary(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        BigDecimal totalIncome = account.getMovements().stream()
                .filter(m -> m.getType() == MovementType.INCOME)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = account.getMovements().stream()
                .filter(m -> m.getType() == MovementType.EXPENSE)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        AccountSummaryDTO summary = new AccountSummaryDTO();
        summary.setName(account.getName());
        summary.setInitialBalance(account.getInitialBalance());
        summary.setCurrentBalance(account.getCurrentBalance());
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpense(totalExpense);
        summary.setMovements(account.getMovements());

        return summary;
    }
}
