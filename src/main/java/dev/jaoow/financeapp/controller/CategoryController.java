package dev.jaoow.financeapp.controller;

import dev.jaoow.financeapp.entity.User;
import dev.jaoow.financeapp.model.dto.request.CategoryRequestDTO;
import dev.jaoow.financeapp.model.dto.response.CategoryResponseDTO;
import dev.jaoow.financeapp.service.CategoryService;
import dev.jaoow.financeapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "API for managing categories")
@RequiredArgsConstructor
public class CategoryController {

    private final UserService userService;
    private final CategoryService categoryService;

    @Operation(summary = "List all categories", description = "Lists all categories for the currently authenticated user, including standard categories.")
    @GetMapping
    public List<CategoryResponseDTO> getAllCategoriesForCurrentUser(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return categoryService.getAllCategoriesForUser(currentUser);
    }

    @Operation(summary = "Get category by ID", description = "Retrieves the details of a specific category.")
    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id, Principal principal) {
        return categoryService.getCategoryByIdAndUser(id, principal.getName());
    }

    @Operation(summary = "Create new category", description = "Creates a new category. Use `isStandard` to create a standard category (only accessible by admins).")
    @PostMapping
    public CategoryResponseDTO createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return categoryService.createCategoryForUser(categoryRequestDTO, currentUser);
    }

    @Operation(summary = "Update category", description = "Updates an existing category.")
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO, Principal principal) {
        return categoryService.updateCategoryForUser(id, categoryRequestDTO, principal.getName());
    }

    @Operation(summary = "Delete category", description = "Deletes an existing category.")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id, Principal principal) {
        categoryService.deleteCategoryForUser(id, principal.getName());
    }
}
