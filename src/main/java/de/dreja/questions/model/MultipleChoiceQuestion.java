package de.dreja.questions.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import de.dreja.common.model.HasId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

public class MultipleChoiceQuestion implements Question {
    protected static final String TYPE_NAME = "multiple-choice";

    private String id;
    private String text;
    private final NavigableMap<String, AnswerOption> options = new TreeMap<>();
    private String correctAnswerId;

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
    public MultipleChoiceQuestion setText(@Nullable String text) {
        this.text = text == null ? "" : text;
        this.id = Question.buildId(TYPE_NAME, this.text);
        return this;
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
    public MultipleChoiceQuestion setOptions(@Nullable Collection<AnswerOption> options) {
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
    }
}
