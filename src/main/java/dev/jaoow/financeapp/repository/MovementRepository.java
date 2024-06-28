package dev.jaoow.financeapp.repository;

import dev.jaoow.financeapp.entity.Account;
import dev.jaoow.financeapp.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long>, JpaSpecificationExecutor<Movement> {

    List<Movement> findByAccount(Account account);

}
