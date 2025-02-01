package de.dreja.quiz.model.json.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dreja.quiz.model.common.Color;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class EventToJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource
    @Tag("unit")
    void testEventToJson(GameEventDto eventDto) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(eventDto);
        } catch (JsonProcessingException e) {
            fail("JSON Write Exception", e);
        }
        assertThat(json).isNotNull().isNotBlank();

        GameEventDto restored = null;
        try {
            restored = objectMapper.readerFor(GameEventDto.class).readValue(json);
        } catch (JsonProcessingException e) {
            fail("JSON Read Exception", e);
        }
        assertThat(restored).isNotNull().isEqualTo(eventDto);
    }

    @Nonnull
    static Stream<GameEventDto> testEventToJson() {
        return Stream.of(
                new AddPlayerEventDto("Thomas"),
                new AddTeamEventDto(null),
                new AddTeamEventDto(Color.BLUE)
        );
    }
}
