package de.dreja.quiz.model;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.dreja.common.model.HasId;
import de.dreja.common.model.IdBase32;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class Quiz implements HasId<IdBase32> {
    
    private IdBase32 id;
    private String name;
    private String author;
    private LocalDateTime lastUpdate;
    private final List<Category> categories = new ArrayList<>();

    @Override
    @Nonnull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public IdBase32 getId() {
        return id;
    }

    @Nonnull
    public Quiz setId(@Nonnull IdBase32 id) {
        this.id = id;
        return this;
    }

    @Nonnull
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Nonnull
    public Quiz setCategories(@Nullable Collection<Category> categories) {
        this.categories.clear();
        if(categories != null) {
            this.categories.addAll(categories);
        }
        return this;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Quiz setName(@Nullable String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    @Nonnull
    public String getAuthor() {
        return author;
    }

    @Nonnull
    public Quiz setAuthor(@Nullable String author) {
        this.author = author == null ? "" : author;
        return this;
    }

    @Nonnull
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Nonnull
    public Quiz setLastUpdate(@Nullable LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate == null ? LocalDateTime.now() : lastUpdate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
