package de.dreja.quiz.model.json.event;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum EventType {

    // Events for each round
    START_ROUND,
    ACTIVATE_BUZZER,
    REQUEST_ATTEMPT,
    SKIP_ROUND,
    CLOSE_ROUND,

    // Events for question types
    SELECT_FROM_MULTIPLE_CHOICE,
    SUBMIT_ESTIMATE,

    // Events for player setup
    ADD_PLAYER,
    REMOVE_PLAYER,
    RENAME_PLAYER,
    REROLL_EMOJI,

    // Events for team setup
    ADD_TEAM,
    REMOVE_TEAM,
    SHUFFLE_TEAMS
}
