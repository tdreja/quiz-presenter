package de.dreja.quiz.model.persistence.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.dreja.quiz.model.persistence.LocalizedEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "section")
public class Section extends LocalizedEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = Quiz.class, optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToMany(targetEntity = Question.class)
    @JoinTable(name = "questions_in_section",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private final List<Question> questions = new ArrayList<>();

    @Nonnull
    @Override
    public Section setLocale(@Nullable Locale locale) {
        super.setLocale(locale);
        return this;
    }

    @Nonnull
    public Quiz getQuiz() {
        return quiz;
    }

    protected void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Nonnull
    public List<Question> getQuestions() {
        return questions;
    }

    @Nonnull
    public Section addQuestion(@Nonnull Question question) {
        if (questions.contains(question)) {
            return this;
        }
        questions.add(question);
        return this;
    }

    @Nonnull
    public Section removeQuestion(@Nonnull Question question) {
        questions.remove(question);
        return this;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Section setName(@Nonnull String name) {
        this.name = name;
        return this;
    }
}
