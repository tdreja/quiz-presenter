package de.dreja.quiz.interfaces.rest;

import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameId;
import de.dreja.quiz.service.persistence.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("debug")
@Profile("dev")
public class DebugEndpoint {

    private final GameRepository gameRepository;

    @Autowired
    DebugEndpoint(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable("gameId") String gameId) {
        return gameRepository.findByGameId(GameId.of(gameId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
