package de.dreja.quiz.model.persistence.quiz;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("text_answer")
public class TextAnswer extends Answer {

    @ManyToOne(targetEntity = MultipleChoiceQuestion.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "multiple_choice_question")
    private MultipleChoiceQuestion multipleChoiceQuestion;

    @Column(nullable = false, name = "answer_text")
    private String answerText;

    @Nonnull
    public String getAnswerText() {
        return answerText;
    }

    @Nonnull
    public TextAnswer setAnswerText(@Nonnull String answerText) {
        this.answerText = answerText;
        return this;
    }

    @Nullable
    @JsonIgnore
    public MultipleChoiceQuestion getMultipleChoiceQuestion() {
        return multipleChoiceQuestion;
    }

    protected void setMultipleChoiceQuestion(@Nullable MultipleChoiceQuestion multipleChoiceQuestion) {
        this.multipleChoiceQuestion = multipleChoiceQuestion;
    }

    @Nonnull
    @Override
    public TextAnswer setLocale(@Nullable Locale locale) {
        super.setLocale(locale);
        return this;
    }

    @Nonnull
    @Override
    public TextAnswer setCorrect(boolean correct) {
        super.setCorrect(correct);
        return this;
    }

    @Override
    @Nonnull
    public TextAnswer asTextAnswer() {
        return this;
    }
}
