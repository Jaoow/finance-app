package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.dto.AccountDTO;
import dev.jaoow.financeapp.dto.AccountSummaryDTO;
import dev.jaoow.financeapp.model.Account;
import dev.jaoow.financeapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getAccountBalance(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return account.getCurrentBalance();
    }

    @GetMapping("/{id}/summary")
    public AccountSummaryDTO getAccountSummary(@PathVariable Long id) {
        return accountService.getAccountSummary(id);
    }

    @PostMapping
    public Account createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO);
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

}
