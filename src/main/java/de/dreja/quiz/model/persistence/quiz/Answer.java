package de.dreja.quiz.model.persistence.quiz;

import java.util.Locale;

import de.dreja.quiz.model.persistence.LocalizedEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "answer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "answer_type", discriminatorType = DiscriminatorType.STRING)
public class Answer extends LocalizedEntity {

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    public boolean isCorrect() {
        return correct;
    }

    @Nonnull
    public Answer setCorrect(boolean correct) {
        this.correct = correct;
        return this;
    }

    @Nonnull
    @Override
    public Answer setLocale(@Nullable Locale locale) {
        super.setLocale(locale);
        return this;
    }

    @Nullable
    public TextAnswer asTextAnswer() {
        return null;
    }
}
