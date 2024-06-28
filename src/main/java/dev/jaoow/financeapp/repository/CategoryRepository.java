package dev.jaoow.financeapp.repository;

import dev.jaoow.financeapp.entity.Category;
import dev.jaoow.financeapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCreatorOrCreatorIsNull(User creator);

}
