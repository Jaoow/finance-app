package dev.jaoow.financeapp.service;

import dev.jaoow.financeapp.entity.Category;
import dev.jaoow.financeapp.entity.User;
import dev.jaoow.financeapp.exception.DuplicateCategoryNameException;
import dev.jaoow.financeapp.exception.ResourceNotFoundException;
import dev.jaoow.financeapp.model.dto.request.CategoryRequestDTO;
import dev.jaoow.financeapp.model.dto.response.CategoryResponseDTO;
import dev.jaoow.financeapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryResponseDTO> getAllCategoriesForUser(User user) {
        List<Category> categories = categoryRepository.findByCreatorOrCreatorIsNull(user);
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponseDTO.class))
                .toList();
    }

    public CategoryResponseDTO getCategoryByIdAndUser(Long categoryId, String username) {
        User user = userService.findByUsername(username);

        Category category = categoryRepository.findById(categoryId)
                .filter(cat -> cat.isStandard() || cat.getCreator().equals(user))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or you do not have access"));

        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public CategoryResponseDTO createCategoryForUser(CategoryRequestDTO categoryRequestDTO, User user) {

        boolean exists = categoryRepository.findByCreatorOrCreatorIsNull(user).stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase(categoryRequestDTO.getName()));

        if (exists) {
            throw new DuplicateCategoryNameException("A category with this name already exists");
        }

        Category category = modelMapper.map(categoryRequestDTO, Category.class);

        if (userService.isAdmin(user.getEmail()) && categoryRequestDTO.isStandard()) {
            category.setCreator(null);
        } else {
            category.setCreator(user);
        }

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDTO.class);
    }

    public CategoryResponseDTO updateCategoryForUser(Long categoryId, CategoryRequestDTO categoryRequestDTO, String username) {
        Category category = categoryRepository.findById(categoryId)
                .filter(cat -> cat.getCreator() == null || cat.getCreator().equals(userService.findByUsername(username)))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or you do not have access"));

        modelMapper.map(categoryRequestDTO, category);

        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryResponseDTO.class);
    }

    public void deleteCategoryForUser(Long categoryId, String username) {
        Category category = categoryRepository.findById(categoryId)
                .filter(cat -> cat.getCreator() == null || cat.getCreator().equals(userService.findByUsername(username)))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found or you do not have access"));

        categoryRepository.delete(category);
    }
}
