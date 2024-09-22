package de.dreja.quiz.service.persistence.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dreja.quiz.model.persistence.quiz.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
