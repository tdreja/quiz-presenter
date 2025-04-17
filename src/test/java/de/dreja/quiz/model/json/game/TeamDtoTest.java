package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.DtoTest;
import org.junit.jupiter.api.Test;

import java.util.List;

class TeamDtoTest extends DtoTest<TeamDto> {

    TeamDtoTest() {
        super(new ObjectMapper(), new TypeReference<>() {
        });
    }

    @Test
    void test() {
        assertJson(new TeamDto(Color.BLUE, 123, 1, List.of(Emoji.CROCODILE), null, false));
        assertJson(new TeamDto(Color.RED, 0, 10, List.of(Emoji.CROCODILE, Emoji.EAGLE), "ABC", true));
    }
}
