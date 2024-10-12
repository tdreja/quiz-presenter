package de.dreja.quiz.model.persistence.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("multiple_choice")
public class MultipleChoiceQuestion extends Question {

    @OneToMany(targetEntity = TextAnswer.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "multipleChoiceQuestion")
    private final List<TextAnswer> answerOptions = new ArrayList<>();

    @Nonnull
    public List<TextAnswer> getAnswerOptions() {
        return answerOptions;
    }

    @Nonnull
    public MultipleChoiceQuestion addAnswerOption(@Nonnull TextAnswer answerOption) {
        answerOption.setMultipleChoiceQuestion(this);
        if (answerOptions.contains(answerOption)) {
            return this;
        }
        answerOptions.add(answerOption);
        return this;
    }

    @Nonnull
    public MultipleChoiceQuestion removeAnswerOption(@Nonnull TextAnswer answerOption) {
        if (answerOptions.remove(answerOption)) {
            answerOption.setMultipleChoiceQuestion(null);
        }
        return this;
    }

    @Override
    @Nonnull
    public MultipleChoiceQuestion setCorrectAnswer(@Nonnull Answer correctAnswer) {
        if (correctAnswer instanceof TextAnswer) {
            super.setCorrectAnswer(correctAnswer);
            return this;
        }
        throw new IllegalArgumentException("MultipleChoiceQuestion can only use TextAnswer!");
    }

    @Nonnull
    @Override
    public MultipleChoiceQuestion setLocale(@Nullable Locale locale) {
        super.setLocale(locale);
        return this;
    }

    @Nonnull
    @Override
    public MultipleChoiceQuestion setDifficulty(int difficulty) {
        super.setDifficulty(difficulty);
        return this;
    }

    @Nonnull
    @Override
    public MultipleChoiceQuestion setQuestionText(@Nonnull String questionText) {
        super.setQuestionText(questionText);
        return this;
    }

    @Override
    @Nonnull
    public MultipleChoiceQuestion asMultipleChoiceQuestion() {
        return this;
    }

    @Override
    public boolean isAnswerCorrect(@Nonnull String answerByTeam) {
        return Optional.ofNullable(getCorrectAnswer().asTextAnswer())
            .map(TextAnswer::getAnswerText)
            .filter(text -> text.equals(answerByTeam))
            .isPresent();
    }

    
}
