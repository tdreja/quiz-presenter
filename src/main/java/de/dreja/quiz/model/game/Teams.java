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
    public Team getNextInOrder(@Nonnull Game game) {
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
    public Team getRandomTeam(@Nonnull Game game) {
        final Team activeTeam = game.getActiveTeam();
        final int size = game.getTeams().size();
        if(size <= 1) {
            if(activeTeam == null) {
                throw new IllegalStateException("No team available found");
            }
            return activeTeam;
        }
        Team selected = game.getTeams().get(random.nextInt(size));
        while(selected.equals(activeTeam)) {
            selected = game.getTeams().get(random.nextInt(size));
        }
        return selected;
    }
}
