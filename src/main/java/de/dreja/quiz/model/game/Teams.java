package de.dreja.quiz.model.game;

import java.util.Random;

import org.springframework.stereotype.Service;

import de.dreja.quiz.model.persistence.game.Game;
import de.dreja.quiz.model.persistence.game.Player;
import de.dreja.quiz.model.persistence.game.Team;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@Service
public class Teams {

    private final Random random;

    Teams() {
        random = new Random();
    }

    public void alterPoints(@Nullable Team team, long additionalPoints) {
        if(team == null) {
            return;
        }
        team.setPoints(team.getPoints() + additionalPoints);
        for(Player player : team.getPlayers()) {
            player.setPoints(player.getPoints() + additionalPoints);
        }
    }

    @Nonnull
    public Team getNextTeam(@Nonnull Game game, @Nullable Team previousWinner) {
        return switch (game.getSelectionMode()) {
            case WINNER -> previousWinner == null ? nextInOrder(game) : previousWinner;
            case RANDOM -> randomTeam(game);
            case NEXT_IN_ORDER -> nextInOrder(game);
            default -> nextInOrder(game);
        };
    }

    @Nonnull
    protected Team nextInOrder(@Nonnull Game game) {
        final Team activeTeam = game.getActiveTeam();
        if (activeTeam == null) {
            return game.getOrderedTeams().findFirst().orElseThrow();
        }
        final Team nextHighest = game.getOrderedTeams()
                .filter(team -> team.getOrderNumber() > activeTeam.getOrderNumber())
                .findFirst()
                .orElse(null);
        if(nextHighest != null) {
            return nextHighest;
        }
        return game.getTeams().getFirst();
    }

    @Nonnull
    protected Team randomTeam(@Nonnull Game game) {
        final Team activeTeam = game.getActiveTeam();
        final int size = game.getTeams().size();
        Team selected = game.getTeams().get(random.nextInt(size));
        while(selected.equals(activeTeam)) {
            selected = game.getTeams().get(random.nextInt(size));
        }
        return selected;
    }
}
