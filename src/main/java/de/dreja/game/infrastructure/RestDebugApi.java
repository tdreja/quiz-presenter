package de.dreja.game.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dreja.common.model.DbRoot;
import de.dreja.common.model.IdBase64;
import de.dreja.common.service.Storage;
import de.dreja.game.model.Game;



@RestController
@RequestMapping("debug")
public class RestDebugApi {

    @Autowired
    RestDebugApi(Storage storage) {
        this.storage = storage;
    }

    private final Storage storage;
    
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable("gameId") String param) {
        final IdBase64 id = IdBase64.of(param);
        final var existing = storage.getRoot().getGame(id);
        if(existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/game")
    public ResponseEntity<String> postGame(@RequestBody Game game) {
        final IdBase64 nextId = storage.getNextId();
        game.setId(nextId);
        final DbRoot root = storage.getRoot();
        root.getGames().put(nextId, game);
        storage.store(root.getGames());
        return ResponseEntity.ok().body(nextId.toString());
    }
}
