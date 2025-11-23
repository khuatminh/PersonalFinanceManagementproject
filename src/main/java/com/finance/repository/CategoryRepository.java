package com.finance.repository;

import com.finance.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByType(Category.CategoryType type);

    java.util.Optional<Category> findByNameIgnoreCase(String name);
}
