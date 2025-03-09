package de.dreja.quiz.service.persistence;

import de.dreja.quiz.model.common.Emoji;
import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Team;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class GameService {

    @Nonnull
    public Optional<Emoji> reserveAvailableEmoji(@Nonnull Game game) {
        return Optional.empty();
    }

    @Nonnull
    public Optional<Team> findSmallestTeam(@Nonnull Game game) {
        return game.getTeams().stream()
                .min(Comparator.comparingInt(t -> t.getPlayers().size()));
    }
}
