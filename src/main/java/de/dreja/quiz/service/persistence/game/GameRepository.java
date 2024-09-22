package de.dreja.quiz.service.persistence.game;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dreja.quiz.model.persistence.game.Game;
import jakarta.annotation.Nonnull;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Nonnull
    Optional<Game> findByGameCode(@Nonnull String gameCode);

    @Query("SELECT g.gameCode FROM Game g")
    List<String> findAllGameCodes();
}
