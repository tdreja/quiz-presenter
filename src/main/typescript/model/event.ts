import { Game } from "./game";
import { AnswerId } from "./question";
import { GameRound, RoundState } from "./round";
import { TeamColor } from "./team";

export enum EventType {
    START_ROUND = 'start-round',
    ACTIVATE_BUZZER = 'activate-buzzer',
    REQUEST_ATTEMPT = 'request-attempt',
    COMPLETE_ATTEMPT = 'complete-attempt'
}

export abstract class GameEvent extends Event {

    protected constructor(type: EventType, eventInitDict?: EventInit) {
        super(type, eventInitDict);
    }

    public abstract updateGame(game: Game): boolean
}

export class StartRoundEvent extends GameEvent {

    private readonly _sectionName: string;
    private readonly _roundPoints: number;
   
    public constructor(sectionName: string, roundPoints: string, eventInitDict?: EventInit) {
        super(EventType.START_ROUND, eventInitDict);
        this._sectionName = sectionName;
        this._roundPoints = Number(roundPoints);
    }

    public updateGame(game: Game): boolean {
        // Force complete the old round
        if(game.round) {
            const oldRound = game.round;
            game.round = null;
            oldRound.state = RoundState.COMPLETED;
        }

        const section = game.sections.find(s => s.name === this._sectionName);
        if(!section) {
            return false;
        }
        const round = section.rounds.find(r => r.pointsForCompletion === this._roundPoints);
        if(!round || round.state !== RoundState.WAIT_ON_REVEAL) {
            return false;
        }
        round.state = RoundState.SHOWING_TEXT;
        game.round = round;
    }
}

abstract class GameRoundEvent extends GameEvent {

    protected constructor(type: EventType, eventInitDict?: EventInit) {
        super(type, eventInitDict);
    }

    public updateGame(game: Game): boolean {
        const round = game.round;
        if(round) {
            return this.updateRound(game, round);
        }
        return false;
    }

    public abstract updateRound(game: Game, round: GameRound): boolean
}

export class ActivateBuzzerEvent extends GameRoundEvent {

    public constructor(eventInitDict?: EventInit) {
        super(EventType.ACTIVATE_BUZZER, eventInitDict);
    }

    public updateRound(game: Game, round: GameRound): boolean {
        if(round.state === RoundState.SHOWING_TEXT || round.state === RoundState.TEAM_CAN_ATTEMPT) {
            round.state = RoundState.BUZZER_ACTIVE;
            return true;
        }
        return false;
    }

}

export class RequestAttemptEvent extends GameRoundEvent {

    private readonly _team: TeamColor;

    public constructor(team: string, eventInitDict?: EventInit) {
        super(EventType.REQUEST_ATTEMPT, eventInitDict);
        this._team = team as TeamColor;
    }

    public updateRound(game: Game, round: GameRound): boolean {
        if(round.state !== RoundState.BUZZER_ACTIVE) {
            return false;
        }
        const team = game.teams.find(t => t.color === this._team);
        if(!team) {
            return false;
        }
        if(round.attemptsBy.includes(team)) {
            return false;
        }
        round.currentlyAttempting = team;
        round.attemptsBy.push(team);
        round.state = RoundState.TEAM_CAN_ATTEMPT;
        return true;
    }

}

export class CompleteAttemptEvent extends GameRoundEvent {

    private readonly _success: boolean; 
    private readonly _answerId?: AnswerId;

    public constructor(success: boolean, answerId?: string, eventInitDict?: EventInit) {
        super(EventType.COMPLETE_ATTEMPT, eventInitDict);
        this._success = success;
        this._answerId = answerId ? answerId as AnswerId : undefined;
    }

    public updateRound(game: Game, round: GameRound): boolean {
        if(round.state !== RoundState.TEAM_CAN_ATTEMPT) {
            return false;
        }
        if(this._answerId) {
            round.usedAnswers.push(this._answerId);
        }
        const team = round.currentlyAttempting;
        if(!team) {
            return false;
        }
        round.currentlyAttempting = null;
        if(!this._success) {
            round.state = RoundState.SHOWING_TEXT;
            return true;
        }


        round.state = RoundState.COMPLETED;
        round.completedBy = team;
        game.round = null;
        
        team.points = team.points + round.pointsForCompletion;

        return true;
    }
}