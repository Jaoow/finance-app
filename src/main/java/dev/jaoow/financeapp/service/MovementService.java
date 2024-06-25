package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.dto.MovementDTO;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.Account;
import dev.jaoow.financeapp.model.Category;
import dev.jaoow.financeapp.model.Movement;
import dev.jaoow.financeapp.model.MovementType;
import dev.jaoow.financeapp.repository.AccountRepository;
import dev.jaoow.financeapp.repository.CategoryRepository;
import dev.jaoow.financeapp.repository.MovementRepository;
import dev.jaoow.financeapp.specification.MovementSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementRepository movementRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    public Movement createMovement(MovementDTO movementDTO) {
        Category category = categoryRepository.findById(movementDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Account account = accountRepository.findById(movementDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Movement movement = new Movement();
        movement.setDescription(movementDTO.getDescription());
        movement.setAmount(movementDTO.getAmount());
        movement.setDate(movementDTO.getDate());
        movement.setCategory(category);
        movement.setAccount(account);
        movement.setType(movementDTO.getType());

        return movementRepository.save(movement);
    }

    public Movement updateMovement(Long id, MovementDTO movementDTO) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));

        Category category = categoryRepository.findById(movementDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Account account = accountRepository.findById(movementDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        movement.setDescription(movementDTO.getDescription());
        movement.setAmount(movementDTO.getAmount());
        movement.setDate(movementDTO.getDate());
        movement.setCategory(category);
        movement.setAccount(account);
        movement.setType(movementDTO.getType());

        return movementRepository.save(movement);
    }

    public void deleteMovement(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));
        movementRepository.delete(movement);
    }

    public Page<Movement> findMovementsByCriteria(Long categoryId, MovementType type, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Movement> specification = Specification.where(
                MovementSpecifications.hasCategory(categoryId)
                        .and(MovementSpecifications.hasType(type))
                        .and(MovementSpecifications.hasDateBetween(startDate, endDate))
        );

        return movementRepository.findAll(specification, pageable);
    }
}
