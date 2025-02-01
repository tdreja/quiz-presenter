package de.dreja.quiz.model.json.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.List;

public record TextMultipleChoiceQuestionDto(@JsonProperty(required = true)
                                            long points,
                                            @JsonProperty(required = true)
                                            @Nonnull
                                            String questionText,
                                            @JsonProperty(required = true)
                                            @Nonnull
                                            List<AnswerOptionDto> answerOptions,
                                            @Nonnull
                                            @JsonProperty(required = true)
                                            List<TeamAnswer> givenAnswers) implements QuestionDto {
}
