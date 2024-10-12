package de.dreja.quiz.interfaces.rest;

import de.dreja.quiz.model.persistence.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dreja.quiz.model.game.IsGameMode;
import de.dreja.quiz.service.persistence.game.GameRepository;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("api/v1")
public class InteractionEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(InteractionEndpoint.class);

    private final ApplicationContext applicationContext;
    private final GameRepository gameRepository;

    @Autowired
    InteractionEndpoint(ApplicationContext applicationContext,
                        GameRepository gameRepository) {
        this.applicationContext = applicationContext;
        this.gameRepository = gameRepository;
    }

    @PutMapping(value = "game/{gameId}/current-question/{questionId}")
    @Transactional
    public ResponseEntity<Boolean> selectQuestion(@PathVariable("gameId") String gameId,
                                                  @PathVariable("questionId") Long questionId) {
        final Game game = gameRepository.findById(GameId.of(gameId).longValue()).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        final GameQuestion selectedQuestion = game.getSections().stream()
                .flatMap(section -> section.getQuestions().stream())
                .filter(question -> question.getId() == questionId)
                .findFirst()
                .orElse(null);
        if (selectedQuestion == null) {
            return ResponseEntity.notFound().build();
        }
        if (selectedQuestion.isAnswered()) {
            return ResponseEntity.badRequest().build();
        }
        final IsGameMode gameMode = applicationContext.getBean(game.getGameMode());
        if (gameMode.onQuestionSelected(game, selectedQuestion)) {
            gameRepository.save(game);
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "game/{gameId}/interactive")
    @Transactional
    public ResponseEntity<Boolean> makeInteractive(@PathVariable("gameId") String gameId) {
        final Game game = gameRepository.findById(GameId.of(gameId).longValue()).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        game.setWaitForTeamInput(true);
        gameRepository.save(game);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping(value = "game/{gameId}/active-team/{team}")
    @Transactional
    public ResponseEntity<Boolean> makeTeamActive(@PathVariable("gameId") String gameId,
                                                  @PathVariable("team") Color color) {
        final Game game = gameRepository.findById(GameId.of(gameId).longValue()).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        final Team team = game.getTeams().stream()
                .filter(t -> t.getColor() == color)
                .findFirst()
                .orElse(null);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        game.setActiveTeam(team);
        gameRepository.save(game);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping(value = "game/{gameId}/current-question/answer/{answerId}")
    @Transactional
    public ResponseEntity<Boolean> tryToAnswer(@PathVariable("gameId") String gameId,
                                               @PathVariable("answerId") Long answerId) {
        final Game game = gameRepository.findById(GameId.of(gameId).longValue()).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        final GameQuestion question = game.getCurrentQuestion();
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        final IsGameMode gameMode = applicationContext.getBean(game.getGameMode());
        if (!answerId.equals(question.getQuestion().getCorrectAnswer().getId())) {
            gameMode.onWrongAnswer(game);
            gameRepository.save(game);
            return ResponseEntity.ok().body(false);
        } else {
            gameMode.onCorrectAnswer(game);
            gameRepository.save(game);
            return ResponseEntity.ok().body(true);
        }
    }
}
