package dev.jaoow.financeapp.repository;

import dev.jaoow.financeapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
