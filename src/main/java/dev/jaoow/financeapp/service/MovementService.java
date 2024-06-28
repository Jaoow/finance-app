package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.entity.Account;
import dev.jaoow.financeapp.entity.Category;
import dev.jaoow.financeapp.entity.Movement;
import dev.jaoow.financeapp.entity.User;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.MovementType;
import dev.jaoow.financeapp.model.dto.request.MovementRequestDTO;
import dev.jaoow.financeapp.model.dto.response.MovementResponseDTO;
import dev.jaoow.financeapp.repository.AccountRepository;
import dev.jaoow.financeapp.repository.CategoryRepository;
import dev.jaoow.financeapp.repository.MovementRepository;
import dev.jaoow.financeapp.specification.MovementSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final UserService userService;
    private final MovementRepository movementRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public List<MovementResponseDTO> getAllMovementsByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        List<Movement> movements = movementRepository.findByAccount(account);
        return movements.stream()
                .map(movement -> modelMapper.map(movement, MovementResponseDTO.class))
                .collect(Collectors.toList());
    }

    public MovementResponseDTO createMovementForAccount(Long accountId, MovementRequestDTO movementDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Movement movement = modelMapper.map(movementDTO, Movement.class);
        movement.setAccount(account);

        if (movementDTO.getCategoryId() != null) {
            Category category = findAccessibleCategory(movementDTO.getCategoryId(), account.getUser().getEmail());
            movement.setCategory(category);
        }

        movement = movementRepository.save(movement);

        return modelMapper.map(movement, MovementResponseDTO.class);
    }

    public MovementResponseDTO updateMovementForAccount(Long accountId, Long movementId, MovementRequestDTO movementDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Movement movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));

        if (!movement.getAccount().getId().equals(accountId)) {
            throw new ResourceNotFoundException("Movement does not belong to the specified account");
        }

        modelMapper.map(movementDTO, movement);
        movement.setAccount(account);

        if (movementDTO.getCategoryId() != null) {
            Category category = findAccessibleCategory(movementDTO.getCategoryId(), account.getUser().getEmail());
            movement.setCategory(category);
        }

        movement = movementRepository.save(movement);

        return modelMapper.map(movement, MovementResponseDTO.class);
    }

    public void deleteMovementForAccount(Long accountId, Long movementId) {
        Movement movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));

        if (!movement.getAccount().getId().equals(accountId)) {
            throw new ResourceNotFoundException("Movement does not belong to the specified account");
        }

        movementRepository.delete(movement);
    }

    private Category findAccessibleCategory(Long categoryId, String username) {
        User user = userService.findByUsername(username);

        return categoryRepository.findById(categoryId)
                .filter(category -> category.isStandard() || category.getCreator().equals(user))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or you do not have access"));
    }

    public Page<MovementResponseDTO> findMovementsByCriteria(Long accountId, Long categoryId, String type, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Movement> specification = Specification.where(
                MovementSpecifications.hasAccount(accountId)
                        .and(MovementSpecifications.hasCategory(categoryId))
                        .and(MovementSpecifications.hasType(MovementType.valueOf(type.toUpperCase())))
                        .and(MovementSpecifications.hasDateBetween(startDate, endDate))
        );

        Page<Movement> page = movementRepository.findAll(specification, pageable);
        return page.map(movement -> modelMapper.map(movement, MovementResponseDTO.class));
    }
}
