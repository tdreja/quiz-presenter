import {Team, TeamUpdate, updateTeams} from "./team";
import {Player} from "./player";

export interface Game {
    teams: Array<Team>
    allPlayers: Array<Player>,
    sections: Array<GameSection>
}

export enum RoundAction {
    NONE,
    READING_QUESTION,
    BUZZER_ACTIVE,
    TEAM_CAN_ANSWER
}

export interface GameRound {
    complete: boolean,
    completedBy: Array<Team>,
    points: number,
    currentAction: RoundAction,
    teamWantsToAnswer?: Team,
    teamTriedToAnswer: Array<Team>
}

export interface GameSection {
    name: string,
    rounds: Array<GameRound>
    complete: boolean
}

export function updateGame(game: Game, teamUpdates: Array<TeamUpdate>, playerUpdates: Array<Player>) {
    game.allPlayers = playerUpdates;
    game.teams = updateTeams(game.teams, game.allPlayers, teamUpdates);
}