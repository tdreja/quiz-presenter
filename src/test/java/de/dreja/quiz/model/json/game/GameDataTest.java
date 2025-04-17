package de.dreja.quiz.model.json.game;

import de.dreja.quiz.model.common.Color;
import de.dreja.quiz.model.common.Emoji;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GameDataTest {

    private final TeamSetup fourPlayersTwoTeams;

    @Autowired
    GameDataTest(TeamSetup fourPlayersTwoTeams) {
        this.fourPlayersTwoTeams = fourPlayersTwoTeams;
    }

    @Test
    void testFourPlayersTwoTeams() {
        assertThat(fourPlayersTwoTeams).isNotNull();

        assertThat(fourPlayersTwoTeams.players())
                .isNotNull()
                .hasSize(4)
                .containsKeys(Emoji.DUCK, Emoji.LEOPARD, Emoji.CROCODILE, Emoji.RABBIT);
        assertThat(fourPlayersTwoTeams.teams())
                .isNotNull()
                .hasSize(2)
                .containsKeys(Color.RED, Color.BLUE);
    }
}
