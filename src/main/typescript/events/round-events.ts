import { Game, GameRound, RoundState } from "../model/game";
import { AnswerId } from "../model/question";
import { Team, TeamColor } from "../model/team";
import { EventType, GameEvent } from "./common-events";

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
        if(game.currentRound) {
            // We're already active?
            if(game.currentRound.pointsForCompletion === this._roundPoints 
                && game.currentRound.inSection === this._sectionName) {
                return false;
            }
            const oldRound = game.currentRound;
            game.currentRound = null;
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
        game.currentRound = round;
        return true;
    }
}

abstract class GameRoundEvent extends GameEvent {

    protected constructor(type: EventType, eventInitDict?: EventInit) {
        super(type, eventInitDict);
    }

    public updateGame(game: Game): boolean {
        const round = game.currentRound;
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
        // Skip non-relevant states
        if(round.state === RoundState.SHOWING_TEXT) {
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
        // Skip non-relevant states
        if(round.state !== RoundState.BUZZER_ACTIVE) {
            return false;
        }
        const team = game.teams.get(this._team);
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
        // Skip non-relevant states
        if(round.state === RoundState.WAIT_ON_REVEAL || round.state === RoundState.COMPLETED) {
            return false;
        }
        // Always mark the answer, if possible
        if(this._answerId) {
            round.usedAnswers.push(this._answerId);
        }

        // Do we have an active team?
        const team = round.currentlyAttempting;
        if(team) {
            if(this._success) {
                this.teamSuccessful(game, round, team);
            } else {
                this.teamNotSuccessful(round, team);
            }
            return true;
        }

        // Round completed by nobody
        if(this._success) {
            return this.nobodySuccessful(game, round);
        }
        // Otherwise we move on
        round.state = RoundState.SHOWING_TEXT;
        round.currentlyAttempting = null;
        return true;
    }

    protected nobodySuccessful(game: Game, round: GameRound): boolean {
        // Complete round
        round.completedBy = null;
        round.state = RoundState.COMPLETED;
        game.currentRound = null;
        return true;
    }


    protected teamNotSuccessful(round: GameRound, team: Team) {
        round.state = RoundState.SHOWING_TEXT;
        round.currentlyAttempting = null;
    }

    protected teamSuccessful(game: Game, round: GameRound, team: Team) {
        // Complete round
        round.state = RoundState.COMPLETED;
        round.completedBy = team;
        round.currentlyAttempting = null;

        // Give points to team and players
        team.points += round.pointsForCompletion;
        team.players.forEach(p => p.points += round.pointsForCompletion);

        // Prepare game for next round
        game.currentRound = null;
    }
}