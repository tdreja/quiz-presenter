package de.dreja.questions.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.math.BigInteger;
import java.util.Objects;

public class EstimateQuestion implements Question {

    protected static final String TYPE_NAME = "estimate";

    private final String id;
    private final String text;

    private BigInteger answer = BigInteger.ZERO;
    private String answerUnit = "";
    private int difficulty;

    @JsonCreator
    public EstimateQuestion(String text) {
        this.text = text == null ? "" : text;
        this.id = Question.buildId(TYPE_NAME, this.text);
    }

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
    @JsonIgnore
    public BigInteger getAnswer() {
        return answer;
    }

    @JsonGetter("answer")
    protected long getAnswerAsLong() {
        return answer.longValue();
    }

    @Nonnull
    public EstimateQuestion setAnswer(@Nullable BigInteger answer) {
        this.answer = answer == null ? BigInteger.ZERO : answer;
        return this;
    }

    @JsonSetter("answer")
    protected void setAnswerLong(long value) {
        this.answer = BigInteger.valueOf(value);
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

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Nonnull
    public EstimateQuestion setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
        EstimateQuestion that = (EstimateQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
