package de.dreja.quiz.model.json.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "optionType",
        include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextAnswerOptionDto.class, name = "text-option"),
})
@Schema(name = "AnswerOption", description = "Option of a multiple-choice question")
public interface AnswerOptionDto {

    @Nonnull
    @JsonProperty(required = true)
    String answerId();

    @JsonProperty(required = true)
    boolean correct();
}
