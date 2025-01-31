package de.dreja.quiz.model.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum GameState {
    TEAM_SETUP,
    PLAYER_SETUP,
    CONTROLLER_SETUP,
    GAME_ACTIVE
}
