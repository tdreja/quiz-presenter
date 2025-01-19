import {Team, TeamColor} from "./team";
import {Emoji, Player} from "./player";
import { Question } from "./question";

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

export function completeRound(game: Game, teams: Array<TeamColor>): boolean {
    if(!game.currentRound) {
        return false;
    }
    
    game.currentRound.completedBy.clear();
    const points = game.currentRound.question.pointsForCompletion;
    for(let teamColor of teams) {
        const team = game.teams.get(teamColor);
        if(!team) {
            continue;
        }
        game.currentRound.completedBy.add(teamColor);
        team.points += points;
        team.players.forEach(player => player.points += points);
    }

    game.currentRound.state = RoundState.COMPLETED;
    game.currentRound.currentlyAttempting.clear();
    game.currentRound = null;
    return true;
}