package de.dreja.quiz.interfaces.web;

import java.time.LocalDate;
import java.time.LocalTime;

import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameId;
import de.dreja.quiz.service.persistence.game.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class GameEndpoint {

    private final GameRepository gameRepository;

    @Autowired
    GameEndpoint(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping({"/","/index"})
    public String getMainPage(Model model) {
        model.addAttribute("now", LocalDate.now());
        return "index";
    }

    @GetMapping("/game/{gameId}")
    @Transactional
    public String getGameOuterPage(@PathVariable("gameId") String gameId, Model model) {
        if(!gameRepository.existsByGameId(GameId.of(gameId))) {
            throw new WebException(HttpStatus.NOT_FOUND, "Game was not found");
        }
        model.addAttribute("gameId", gameId);
        return "game";
    }

    @GetMapping("/game/{gameId}/content")
    @Transactional
    public String getGameInnerPage(@PathVariable("gameId") String gameId, Model model) {
        final var search = gameRepository.findByGameId(GameId.of(gameId));
        if(search.isEmpty()) {
            throw new WebException(HttpStatus.NOT_FOUND, "Game was not found");
        }
        final var game = search.get();
        model.addAttribute("gameId", gameId);
        model.addAttribute("game", game);
        model.addAttribute("quiz", game.getQuiz());
        return "game-content";
    }
}
