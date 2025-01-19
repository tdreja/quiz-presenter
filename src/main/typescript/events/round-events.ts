import { Game, GameRound, RoundState } from "../model/game";
import { AnswerId } from "../model/question";
import { Team, TeamColor } from "../model/team";
import { EventType, GameEvent } from "./common-events";

/**
 * Admin selects a new question to play
 */
export class StartRoundEvent extends GameEvent {

    private readonly _sectionName: string;
    private readonly _questionId: string;
   
    public constructor(sectionName: string, questionId: string, eventInitDict?: EventInit) {
        super(EventType.START_ROUND, eventInitDict);
        this._sectionName = sectionName;
        this._questionId = questionId;
    }

    public updateGame(game: Game): boolean {
        // Force complete the old round
        if(game.currentRound) {
            // We're already active?
            if(game.currentRound.question.questionId === this._questionId 
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
        const round = section.rounds.find(r => r.question.questionId === this._questionId);
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

/**
 * Admin allows the buzzer to be pressed
 */
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

/** 
 * One team pressed the buzzer button and wants to answer
 */
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
        if(!game.teams.get(this._team)) {
            return false;
        }
        if(round.alreadyAttempted.has(this._team)) {
            return false;
        }
        round.currentlyAttempting.clear();
        round.currentlyAttempting.add(this._team);
        round.alreadyAttempted.add(this._team);
        round.state = RoundState.TEAM_CAN_ATTEMPT;
        return true;
    }
}