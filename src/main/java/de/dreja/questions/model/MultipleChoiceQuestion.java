package de.dreja.questions.model;

import com.fasterxml.jackson.annotation.*;
import de.dreja.common.model.HasId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

public class MultipleChoiceQuestion implements Question {
    protected static final String TYPE_NAME = "multiple-choice";

    private final String id;
    private final String text;
    private final NavigableMap<String, AnswerOption> options = new TreeMap<>();
    private String correctAnswerId;
    private int difficulty;

    @JsonCreator
    public MultipleChoiceQuestion(@JsonProperty("text") String text) {
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
    public NavigableMap<String, AnswerOption> getOptions() {
        return Collections.unmodifiableNavigableMap(options);
    }

    @Nonnull
    @JsonGetter("options")
    protected List<AnswerOption> getOptionsList() {
        return new ArrayList<>(options.values());
    }

    @Nonnull
    @JsonSetter("options")
    public MultipleChoiceQuestion setOptions(@Nullable List<AnswerOption> options) {
        this.options.clear();
        if(options != null) {
            options.forEach(opt -> HasId.put(this.options, opt));
        }
        return this;
    }

    @Nonnull
    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    @JsonIgnore
    @Nonnull
    public Optional<AnswerOption> getCorrectAnswer() {
        return HasId.get(this.options, correctAnswerId);
    }

    @Nonnull
    public MultipleChoiceQuestion setCorrectAnswerId(@Nullable String correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
        return this;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Nonnull
    public MultipleChoiceQuestion setDifficulty(int difficulty) {
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
        MultipleChoiceQuestion that = (MultipleChoiceQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static class AnswerOption implements HasId<String> {

        private String id;
        private String text;

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
        public AnswerOption setText(@Nullable String text) {
            this.text = text == null ? "" : text;
            this.id = Question.buildId("answer", this.text);
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
            AnswerOption that = (AnswerOption) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
}
