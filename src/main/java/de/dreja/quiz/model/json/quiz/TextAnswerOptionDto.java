package de.dreja.quiz.model.json.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

public record TextAnswerOptionDto(@Nonnull
                                  @JsonProperty(required = true)
                                  String answerId,
                                  @Nonnull
                                  @JsonProperty(required = true)
                                  String answerText,
                                  @JsonProperty(required = true)
                                  boolean correct) implements AnswerOptionDto {
}
