package de.dreja.quiz.interfaces.rest;

import de.dreja.quiz.model.json.event.AddPlayerEventDto;
import de.dreja.quiz.model.json.game.GameDto;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("debug")
@Profile("dev")
public class DebugEndpoint {

    @GetMapping("/game/")
    public ResponseEntity<GameDto> getGame() {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/event/")
    public ResponseEntity<AddPlayerEventDto> getEvent() {
        return ResponseEntity.notFound().build();
    }
}
