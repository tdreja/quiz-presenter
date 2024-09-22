package de.dreja.quiz.model.persistence.quiz;

import java.util.Locale;

import de.dreja.quiz.model.persistence.LocalizedEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "question")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
public class Question extends LocalizedEntity {

    @Column(nullable = false)
    private int difficulty = 1;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @OneToOne(targetEntity = Answer.class, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "correct_answer", unique = true, nullable = false, table = "question")
    private Answer correctAnswer;

    @Nonnull
    @Override
    public Question setLocale(@Nullable Locale locale) {
        super.setLocale(locale);
        return this;
    }

    @Nonnull
    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    @Nonnull
    public Question setCorrectAnswer(@Nonnull Answer correctAnswer) {
        if (this.correctAnswer != null) {
            this.correctAnswer.setCorrect(false);
        }
        this.correctAnswer = correctAnswer;
        this.correctAnswer.setCorrect(true);
        return this;
    }

    public int getDifficulty() {
        return difficulty;
    }

    @Nonnull
    public Question setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    @Nonnull
    public String getQuestionText() {
        return questionText;
    }

    @Nonnull
    public Question setQuestionText(@Nonnull String questionText) {
        this.questionText = questionText;
        return this;
    }

    @Nullable
    public MultipleChoiceQuestion asMultipleChoiceQuestion() {
        return null;
    }
}
