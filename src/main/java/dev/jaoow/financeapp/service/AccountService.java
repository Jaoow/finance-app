package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.entity.Account;
import dev.jaoow.financeapp.entity.Movement;
import dev.jaoow.financeapp.entity.User;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.MovementType;
import dev.jaoow.financeapp.model.dto.AccountSummaryDTO;
import dev.jaoow.financeapp.model.dto.GeneralSummaryDTO;
import dev.jaoow.financeapp.model.dto.request.AccountRequestDTO;
import dev.jaoow.financeapp.model.dto.response.AccountResponseDTO;
import dev.jaoow.financeapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    private Account findByIdAndUser(Long accountId, String email) {
        User user = userService.findByUsername(email);

        return accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found or you do not have access"));
    }

    public AccountResponseDTO getByIdAndUser(Long accountId, String email) {
        Account account = findByIdAndUser(accountId, email);
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public List<AccountResponseDTO> getAllByUser(User user) {
        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountResponseDTO.class))
                .toList();
    }

    public BigDecimal getBalance(Long accountId, String username) {
        Account account = findByIdAndUser(accountId, username);
        return account.getCurrentBalance();
    }

    public AccountResponseDTO createForUser(AccountRequestDTO accountDTO, User user) {
        Account account = modelMapper.map(accountDTO, Account.class);
        account.setUser(user);

        account = accountRepository.save(account);
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public AccountResponseDTO updateForUser(Long accountId, AccountRequestDTO accountDTO, String username) {
        Account account = findByIdAndUser(accountId, username);
        modelMapper.map(accountDTO, account);

        account = accountRepository.save(account);
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public void deleteForUser(Long accountId, String username) {
        Account account = findByIdAndUser(accountId, username);
        accountRepository.delete(account);
    }

    public AccountSummaryDTO getSummaryForUser(Long accountId, String username) {
        Account account = findByIdAndUser(accountId, username);
        BigDecimal totalIncome = account.getMovements().stream()
                .filter(m -> m.getType() == MovementType.INCOME)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = account.getMovements().stream()
                .filter(m -> m.getType() == MovementType.EXPENSE)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        AccountSummaryDTO summary = modelMapper.map(account, AccountSummaryDTO.class);
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpense(totalExpense);
        summary.setCurrentBalance(account.getCurrentBalance());

        return summary;
    }

    public GeneralSummaryDTO getGeneralSummary(User user) {
        return getFilteredGeneralSummary(user, null, null);
    }

    public GeneralSummaryDTO getFilteredGeneralSummary(User user, LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountRepository.findByUser(user);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;

        for (Account account : accounts) {
            for (Movement movement : account.getMovements()) {
                boolean isWithinRange = (startDate == null || !movement.getDate().isBefore(startDate)) &&
                        (endDate == null || !movement.getDate().isAfter(endDate));

                if (isWithinRange) {
                    if (movement.getType() == MovementType.INCOME) {
                        totalIncome = totalIncome.add(movement.getAmount());
                    } else if (movement.getType() == MovementType.EXPENSE) {
                        totalExpense = totalExpense.subtract(movement.getAmount());
                    }
                }
            }
            totalBalance = totalBalance.add(account.getCurrentBalance());
        }

        GeneralSummaryDTO summary = new GeneralSummaryDTO();
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpense(totalExpense);
        summary.setCurrentBalance(totalBalance);

        return summary;
    }

    public void verifyUserHasAccessToAccount(Long accountId, String email) {
        User user = userService.findByUsername(email);

        if (!userService.isAdmin(email) && accountRepository.findByIdAndUser(accountId, user).isEmpty()) {
            throw new AccessDeniedException("You do not have access to this resource");
        }
    }
}
