package de.dreja.quiz.model.json.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "questionType",
        include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextMultipleChoiceQuestionDto.class, name = "text-multiple-choice"),
})
@Schema(name = "Question", description = "Question or task asked of the players")
public interface QuestionDto {

    @Nonnull
    @JsonProperty(required = true)
    List<TeamAnswer> givenAnswers();
}
