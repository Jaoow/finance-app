package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.entity.User;
import dev.jaoow.financeapp.model.dto.AccountSummaryDTO;
import dev.jaoow.financeapp.model.dto.GeneralSummaryDTO;
import dev.jaoow.financeapp.model.dto.request.AccountRequestDTO;
import dev.jaoow.financeapp.model.dto.response.AccountResponseDTO;
import dev.jaoow.financeapp.service.AccountService;
import dev.jaoow.financeapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "API for managing user accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @Operation(summary = "List all user accounts", description = "Lists all accounts for the currently authenticated user.")
    @GetMapping
    public List<AccountResponseDTO> getAllAccountsForCurrentUser(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return accountService.getAllByUser(currentUser);
    }

    @Operation(summary = "Get account by ID", description = "Retrieves the details of a specific account.")
    @GetMapping("/{id}")
    public AccountResponseDTO getAccountById(@PathVariable Long id, Principal principal) {
        return accountService.getByIdAndUser(id, principal.getName());
    }

    @Operation(summary = "Create new account", description = "Creates a new account for the currently authenticated user.")
    @PostMapping
    public AccountResponseDTO createAccount(@Valid @RequestBody AccountRequestDTO accountDTO, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return accountService.createForUser(accountDTO, currentUser);
    }

    @Operation(summary = "Update account", description = "Updates the details of an existing account.")
    @PutMapping("/{id}")
    public AccountResponseDTO updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequestDTO accountDTO, Principal principal) {
        return accountService.updateForUser(id, accountDTO, principal.getName());
    }

    @Operation(summary = "Delete account", description = "Deletes an existing account.")
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id, Principal principal) {
        accountService.deleteForUser(id, principal.getName());
    }

    @Operation(summary = "Get account balance", description = "Retrieves the current balance of a specific account.")
    @GetMapping("/{id}/balance")
    public BigDecimal getAccountBalance(@PathVariable Long id, Principal principal) {
        return accountService.getBalance(id, principal.getName());
    }

    @Operation(summary = "Get account summary", description = "Retrieves a summary of a specific account, including total income and expenses.")
    @GetMapping("/{id}/summary")
    public AccountSummaryDTO getAccountSummary(@PathVariable Long id, Principal principal) {
        return accountService.getSummaryForUser(id, principal.getName());
    }

    @Operation(summary = "Get general summary of all accounts", description = "Retrieves a general summary of all accounts for the currently authenticated user.")
    @GetMapping("/summary")
    public GeneralSummaryDTO getGeneralSummary(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return accountService.getGeneralSummary(currentUser);
    }

    @Operation(summary = "Get filtered general summary", description = "Retrieves a general summary of all accounts filtered by date range.")
    @GetMapping("/summary/filter")
    public GeneralSummaryDTO getFilteredGeneralSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return accountService.getFilteredGeneralSummary(currentUser, startDate, endDate);
    }
}
