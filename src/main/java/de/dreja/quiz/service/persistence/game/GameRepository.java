package de.dreja.quiz.service.persistence.game;

import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.GameId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Nonnull
    default Optional<Game> findGameByGameId(@Nonnull GameId gameId) {
        return findById(gameId.getNumerical());
    }

    @Nonnull
    default Optional<Game> findGameByGameId(@Nullable String gameId) {
        return findGameByGameId(GameId.of(gameId));
    }
}
