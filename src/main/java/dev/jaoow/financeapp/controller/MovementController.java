package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.model.dto.request.MovementRequestDTO;
import dev.jaoow.financeapp.model.dto.response.MovementResponseDTO;
import dev.jaoow.financeapp.service.AccountService;
import dev.jaoow.financeapp.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/movements")
@Tag(name = "Movements", description = "API for managing account movements")
@RequiredArgsConstructor
public class MovementController {

    private final AccountService accountService;
    private final MovementService movementService;

    @Operation(summary = "List all movements", description = "Lists all movements for a specific account.")
    @GetMapping
    public List<MovementResponseDTO> getAllMovementsForAccount(@PathVariable Long accountId, Principal principal) {
        accountService.verifyUserHasAccessToAccount(accountId, principal.getName());
        return movementService.getAllMovementsByAccount(accountId);
    }

    @Operation(summary = "Search movements", description = "Searches movements for a specific account based on various criteria.")
    @GetMapping("/search")
    public Page<MovementResponseDTO> searchMovements(
            @PathVariable Long accountId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date,desc") String[] sort,
            Principal principal) {

        accountService.verifyUserHasAccessToAccount(accountId, principal.getName());

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortOrder : sort) {
            String[] sortParams = sortOrder.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            orders.add(new Sort.Order(direction, sortParams[0]));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        return movementService.findMovementsByCriteria(accountId, categoryId, type, startDate, endDate, pageable);
    }

    @Operation(summary = "Create new movement", description = "Creates a new movement for a specific account.")
    @PostMapping
    public MovementResponseDTO createMovement(@PathVariable Long accountId, @Valid @RequestBody MovementRequestDTO movementDTO, Principal principal) {
        accountService.verifyUserHasAccessToAccount(accountId, principal.getName());
        return movementService.createMovementForAccount(accountId, movementDTO);
    }

    @Operation(summary = "Update movement", description = "Updates an existing movement for a specific account.")
    @PutMapping("/{movementId}")
    public MovementResponseDTO updateMovement(@PathVariable Long accountId, @PathVariable Long movementId, @Valid @RequestBody MovementRequestDTO movementDTO, Principal principal) {
        accountService.verifyUserHasAccessToAccount(accountId, principal.getName());
        return movementService.updateMovementForAccount(accountId, movementId, movementDTO);
    }

    @Operation(summary = "Delete movement", description = "Deletes an existing movement for a specific account.")
    @DeleteMapping("/{movementId}")
    public void deleteMovement(@PathVariable Long accountId, @PathVariable Long movementId, Principal principal) {
        accountService.verifyUserHasAccessToAccount(accountId, principal.getName());
        movementService.deleteMovementForAccount(accountId, movementId);
    }
}
