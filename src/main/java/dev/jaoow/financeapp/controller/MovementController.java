package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.dto.MovementDTO;
import dev.jaoow.financeapp.model.Movement;
import dev.jaoow.financeapp.model.MovementType;
import dev.jaoow.financeapp.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping
    public List<Movement> getAllMovements() {
        return movementService.getAllMovements();
    }

    @GetMapping("/search")
    public Page<Movement> searchMovements(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) MovementType type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date,desc") String[] sort) {

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortOrder : sort) {
            String[] sortParams = sortOrder.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            orders.add(new Sort.Order(direction, sortParams[0]));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        return movementService.findMovementsByCriteria(categoryId, type, startDate, endDate, pageable);
    }

    @PostMapping
    public Movement createMovement(@Valid @RequestBody MovementDTO movementDTO) {
        return movementService.createMovement(movementDTO);
    }

    @PutMapping("/{id}")
    public Movement updateMovement(@PathVariable Long id, @Valid @RequestBody MovementDTO movementDTO) {
        return movementService.updateMovement(id, movementDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
    }
}
