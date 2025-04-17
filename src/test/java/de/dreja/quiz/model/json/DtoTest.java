package de.dreja.quiz.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class DtoTest<T> {

    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;

    protected DtoTest(@Nonnull ObjectMapper objectMapper,
                      @Nonnull TypeReference<T> typeReference) {
        this.objectMapper = objectMapper;
        this.typeReference = typeReference;
    }

    @Nonnull
    protected T assertJson(@Nonnull T input) {
        try {
            assertThat(input).isNotNull();
            final String string = objectMapper.writerFor(typeReference).writeValueAsString(input);
            assertThat(string).isNotBlank();

            final T restored = objectMapper.readerFor(typeReference).readValue(string);
            assertThat(restored).isNotNull().isEqualTo(input);

            return restored;
        } catch (JsonProcessingException e) {
            throw new AssertionFailedError("Could not work with JSON", e);
        }
    }
}
