package de.dreja.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.dreja.common.model.HasId;
import de.dreja.questions.model.Question;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

public class Category {

    private String name  = "";
    private final NavigableSet<String> questionIds = new TreeSet<>();
    private transient List<Question> questions;

    public String getName() {
        return name;
    }

    @Nonnull
    public Category setName(@Nullable String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    @Nonnull
    @JsonIgnore
    public NavigableSet<String> getQuestionIds() {
        return Collections.unmodifiableNavigableSet(questionIds);
    }

    @Nonnull
    public Category setQuestionIds(@Nullable Collection<String> questionIds) {
        this.questionIds.clear();
        if(questionIds != null) {
            this.questionIds.addAll(questionIds);
        }
        return this;
    }

    @Nonnull
    public List<Question> getQuestions() {
        if(questions == null) {
            return Collections.emptyList();
        }
        return questions;
    }

    @Nonnull
    public Category setQuestions(@Nullable Map<String, Question> questionMap) {
        this.questions = HasId.stream(questionMap, questionIds).toList();
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
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
