package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.dto.CategoryDTO;
import dev.jaoow.financeapp.exception.DuplicateCategoryNameException;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.Category;
import dev.jaoow.financeapp.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    public CategoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldThrowException_WhenCategoryNameExists() {
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Food");

        when(categoryRepository.existsByName("Food")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.createCategory(categoryDTO));
    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNameExistsForOtherId() {
        // Arrange
        Long id = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Transport");

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("Bills");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByNameAndIdNot("Transport", id)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateCategoryNameException.class, () -> categoryService.updateCategory(id, categoryDTO));
    }

    @Test
    void updateCategory_ShouldUpdate_WhenNameIsUnique() {
        // Arrange
        Long id = 1L;
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Utilities");

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("Bills");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByNameAndIdNot("Utilities", id)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        // Act
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);

        // Assert
        assertEquals("Utilities", updatedCategory.getName());
    }

    @Test
    void findCategoriesByName_ShouldReturnCategories_WhenNamePartiallyMatches() {
        // Arrange
        String nameQuery = "Foo";
        Category category1 = new Category(1L, "Food");
        Category category2 = new Category(2L, "Footwear");

        when(categoryRepository.findByNameContainingIgnoreCase(nameQuery)).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<Category> categories = categoryService.findCategoriesByName(nameQuery);

        // Assert
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Food")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Footwear")));
    }
}
