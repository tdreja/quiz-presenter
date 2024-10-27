import {alterTeamPoints, Team} from "./team";
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

export function startRound(round: GameRound): boolean {
    if(round.complete) {
        return false;
    }
    if(round.currentAction !== RoundAction.NONE) {
        return false;
    }
    round.currentAction = RoundAction.READING_QUESTION;
    return true;
}

export function activateBuzzer(round: GameRound): boolean {
    if(round.complete) {
        return false;
    }
    if(round.currentAction === RoundAction.READING_QUESTION || round.currentAction === RoundAction.TEAM_CAN_ANSWER) {
        round.currentAction = RoundAction.BUZZER_ACTIVE;
        round.teamWantsToAnswer = undefined;
        return true;
    }
    return false;
}

export function teamWantsToAnswer(round: GameRound, team: Team) {
    if(round.complete) {
        return false;
    }
    if(round.currentAction !== RoundAction.BUZZER_ACTIVE) {
        return false;
    }
    if(round.teamTriedToAnswer.includes(team)) {
        return false;
    }
    round.teamTriedToAnswer.push(team);
    round.teamWantsToAnswer = team;
    round.currentAction = RoundAction.TEAM_CAN_ANSWER;
    return true;
}

export function answerReceived(round: GameRound, answer: boolean) {
    if(round.complete) {
        return false;
    }
    if(round.currentAction !== RoundAction.TEAM_CAN_ANSWER) {
        return false;
    }
    if(answer) {
        round.complete = true;
        round.currentAction = RoundAction.NONE;
        const team = round.teamWantsToAnswer;
        if(team) {
            round.completedBy.push(team);
            alterTeamPoints(team, round.points);
        }
    } else {
        round.teamWantsToAnswer = undefined;
        round.currentAction = RoundAction.BUZZER_ACTIVE;
        return true;
    }
}