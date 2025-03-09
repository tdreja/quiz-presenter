package de.dreja.quiz.service.persistence.game;

import de.dreja.quiz.model.persistence.game.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
