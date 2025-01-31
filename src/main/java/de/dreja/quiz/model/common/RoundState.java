package de.dreja.quiz.model.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum RoundState {
    WAIT_ON_REVEAL,
    SHOW_QUESTION,
    BUZZER_ACTIVE,
    TEAM_CAN_ATTEMPT,
    COMPLETE_WITH_RESULTS,
    CLOSED
}
