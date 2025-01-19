import {Team, TeamColor} from "./team";
import {Emoji, Player} from "./player";
import { AnswerId, Question } from "./question";

/**
 * Describes how the attempt went for the team 
 */
export enum AttemptState {
    CURRENTLY_ATTEMPTING,
    SUCCESSFUL,
    FAILURE
}

/**
 * Describes the actions of a team trying to complete the round
 */
export interface TeamAttempt {
    attemptBy: TeamColor,
    state: AttemptState,
    answerId?: AnswerId
}

/**
 * Describes at which point of the current round we are
 */
export enum RoundState {
    WAIT_ON_REVEAL,
    SHOWING_TEXT,
    BUZZER_ACTIVE,
    TEAM_CAN_ATTEMPT,
    COMPLETED
}

/**
 * Contains all relevant data for one round of the quiz (i.e. one question)
 */
export interface GameRound {
    
    readonly question: Question;
    state: RoundState;
    readonly currentlyAttempting: Set<TeamColor>;
    readonly alreadyAttempted: Set<TeamColor>;
    readonly completedBy: Set<TeamColor>;
    readonly inSection: string;

}

/**
 * Group of rounds with a name associated (e.g. category of questions)
 */
export interface GameSection {
    readonly name: string;
    readonly rounds: Array<GameRound>;
}

/**
 * Container with all game data
 */
export interface Game {
    readonly sections: Array<GameSection>;
    readonly availableEmojis: Set<Emoji>;
    readonly availableColors: Set<TeamColor>;
    readonly players: Map<Emoji, Player>;
    readonly teams: Map<TeamColor, Team>;
    selectingTeam: Team | null;
    currentRound: GameRound | null;
}
