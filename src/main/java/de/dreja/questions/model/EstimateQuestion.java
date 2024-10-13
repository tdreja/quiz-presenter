package de.dreja.questions.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.math.BigInteger;

public class EstimateQuestion implements Question {

    protected static final String TYPE_NAME = "estimate";

    private String id;
    private String text;

    private BigInteger answer = BigInteger.ZERO;
    private String answerUnit = "";

    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    public String getText() {
        return text;
    }

    @Nonnull
    public EstimateQuestion setText(@Nullable String text) {
        this.text = text == null ? "" : text;
        this.id = Question.buildId(TYPE_NAME, this.text);
        return this;
    }

    @Nonnull
    public BigInteger getAnswer() {
        return answer;
    }

    @Nonnull
    public EstimateQuestion setAnswer(@Nullable BigInteger answer) {
        this.answer = answer == null ? BigInteger.ZERO : answer;
        return this;
    }

    @Nonnull
    public String getAnswerUnit() {
        return answerUnit;
    }

    @Nonnull
    public EstimateQuestion setAnswerUnit(@Nullable String answerUnit) {
        this.answerUnit = answerUnit == null ? "" : answerUnit;
        return this;
    }
}
