package de.dreja.quiz.model.json.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GameData {

    private final Resource fourPlayersTwoTeams = new ClassPathResource("json/four_players_two_teams.json");

    private final ObjectMapper objectMapper;

    @Autowired
    GameData(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Nonnull
    @Bean("fourPlayersTwoTeams")
    public TeamSetup fourPlayersTwoTeams() {
        return read(TeamSetup.class, fourPlayersTwoTeams);
    }

    @Nonnull
    private <T> T read(@Nonnull Class<T> type, @Nonnull Resource resource) {
        try {
            return objectMapper.readValue(resource.getContentAsByteArray(), type);
        } catch (IOException e) {
            throw new AssertionFailedError("Exception while reading json file", e);
        }
    }
}
