package dev.jaoow.financeapp.repository;

import dev.jaoow.financeapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<Category> findByNameContainingIgnoreCase(String name);
}
