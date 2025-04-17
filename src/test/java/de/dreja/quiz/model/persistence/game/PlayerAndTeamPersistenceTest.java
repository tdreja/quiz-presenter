package de.dreja.quiz.model.persistence.game;

import de.dreja.quiz.model.json.game.PlayerDto;
import de.dreja.quiz.model.json.game.TeamDto;
import de.dreja.quiz.model.json.game.TeamSetup;
import de.dreja.quiz.service.mapping.PlayerMapper;
import de.dreja.quiz.service.mapping.TeamMapper;
import de.dreja.quiz.service.persistence.game.GameRepository;
import de.dreja.quiz.service.persistence.game.PlayerRepository;
import de.dreja.quiz.service.persistence.game.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlayerAndTeamPersistenceTest {

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamMapper teamMapper;
    private final PlayerMapper playerMapper;
    private final TeamSetup fourPlayersTwoTeams;

    private Long gameId;

    @Autowired
    PlayerAndTeamPersistenceTest(GameRepository gameRepository,
                                 TeamRepository teamRepository,
                                 PlayerRepository playerRepository,
                                 TeamMapper teamMapper,
                                 PlayerMapper playerMapper,
                                 TeamSetup fourPlayersTwoTeams) {
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.teamMapper = teamMapper;
        this.playerMapper = playerMapper;
        this.fourPlayersTwoTeams = fourPlayersTwoTeams;
    }

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setupEmptyGame() {
        final Game game = new Game();
        gameRepository.save(game);

        for(TeamDto teamDto : fourPlayersTwoTeams.teams().values()) {
            final Team team = new Team();
            teamMapper.updateDb(teamDto, team, game);
            team.setGame(game);
            teamRepository.save(team);
        }
        gameRepository.save(game);

        for(PlayerDto playerDto : fourPlayersTwoTeams.players().values()) {
            final Player player = new Player();
            playerMapper.updateAttributes(playerDto, player, game);
            player.setGame(game);
            playerRepository.save(player);
        }
        teamRepository.saveAll(game.getTeams());
        gameRepository.save(game);

        gameId = game.getId();
    }

    @Test
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    void testGame() {
        assertThat(gameId).isNotNull();
        final Game game = gameRepository.findById(gameId).orElse(null);
        assertThat(game).isNotNull();

        for(TeamDto teamDto : fourPlayersTwoTeams.teams().values()) {
            final Team team = game.getTeams().stream()
                    .filter(t -> t.getColor() == teamDto.color())
                    .findFirst().orElse(null);
            assertThat(team).isNotNull()
                    .hasFieldOrPropertyWithValue("color", teamDto.color())
                    .hasFieldOrPropertyWithValue("points", teamDto.points())
                    .hasFieldOrPropertyWithValue("nextTurnNumber", teamDto.nextTurnNumber())
                    .hasFieldOrPropertyWithValue("gamepadId", teamDto.gamepadId())
                    .hasFieldOrPropertyWithValue("gamepadRequested", teamDto.gamepadRequested());
            assertThat(team.getPlayers()).hasSize(teamDto.players().size());
        }

        for(PlayerDto playerDto : fourPlayersTwoTeams.players().values()) {
            final Player player = game.getPlayers().stream()
                    .filter(p -> p.getEmoji() == playerDto.emoji())
                    .findFirst().orElse(null);
            assertThat(player).isNotNull()
                    .hasFieldOrPropertyWithValue("emoji", playerDto.emoji())
                    .hasFieldOrPropertyWithValue("points", playerDto.points())
                    .hasFieldOrPropertyWithValue("name", playerDto.name());
            assertThat(player.getTeam()).isNotNull()
                    .hasFieldOrPropertyWithValue("color", playerDto.assignedToTeam());
        }
    }
}
