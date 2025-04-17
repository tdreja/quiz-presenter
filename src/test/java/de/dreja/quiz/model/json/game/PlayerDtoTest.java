package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.json.DtoTest;
import org.junit.jupiter.api.Test;

class PlayerDtoTest extends DtoTest<PlayerDto> {

    PlayerDtoTest() {
        super(new ObjectMapper(), new TypeReference<>() {
        });
    }

    @Test
    void test() {
        assertJson(new PlayerDto(Emoji.BEAVER, "Beaver", 123, Color.BLUE));
        assertJson(new PlayerDto(Emoji.CROCODILE, "", 0, Color.TURQUOISE));
    }
}
