package de.dreja.quiz.model.persistence.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.dreja.quiz.model.persistence.LocalizedEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "quiz")
@Entity
public class Quiz extends LocalizedEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String author;

    @OneToMany(mappedBy = "quiz")
    private final List<Category> categories = new ArrayList<>();

    @Nonnull
    public List<Category> getCategories() {
        return categories;
    }

    @Nonnull
    public Quiz addCategory(@Nonnull Category category) {
        category.setQuiz(this);
        if (categories.contains(category)) {
            return this;
        }
        categories.add(category);
        return this;
    }

    @Nonnull
    public Quiz removeCategory(@Nonnull Category category) {
        if (categories.remove(category)) {
            category.setQuiz(null);
        }
        return this;
    }

    @Nonnull
    @Override
    public Quiz setLocale(@Nullable Locale locale) {
        super.setLocale(locale);
        return this;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Quiz setName(@Nonnull String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public String getAuthor() {
        return author;
    }

    @Nonnull
    public Quiz setAuthor(@Nullable String author) {
        this.author = author;
        return this;
    }
}
