import { AnswerId, Question } from "./question";
import { Team, TeamColor } from "./team";

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
export class GameRound {

    private readonly _pointsForCompletion: number;
    private readonly _question: Question;
    private _state: RoundState;
    private _currentlyAttempting: Team | null;
    private _completedBy: Team | null;
    private readonly _attemptsBy: Array<Team>;
    private readonly _usedAnswers: Array<AnswerId>;

    public get pointsForCompletion(): number {
        return this._pointsForCompletion;
    }
    
    public get state() : RoundState {
        return this._state;
    }
    
    public get attemptsBy() : Array<Team> {
        return this._attemptsBy;
    }

    public get completedBy(): Team | null {
        return this._completedBy;
    }

    public get currentlyAttempting(): Team | null {
        return this._currentlyAttempting;
    }

    public get question(): Question {
        return this._question;
    }

    public constructor(question: Question, pointsForCompletion: number) {
        this._question = question;
        this._pointsForCompletion = pointsForCompletion;
        this._state = RoundState.WAIT_ON_REVEAL;
        this._attemptsBy = [];
        this._usedAnswers = [];
        this._completedBy = null;
        this._currentlyAttempting = null;
    }

    public startRound(): boolean {
        if(this._state === RoundState.WAIT_ON_REVEAL) {
            this._state = RoundState.SHOWING_TEXT;
            return true;
        }
        return false;
    }

    public activateBuzzer(): boolean {
        if(this._state === RoundState.SHOWING_TEXT || this._state === RoundState.TEAM_CAN_ATTEMPT) {
            this._state = RoundState.BUZZER_ACTIVE;
            return true;
        }
        return false;
    }

    public requestAttempt(team: Team): boolean {
        if(this._state !== RoundState.BUZZER_ACTIVE) {
            return false;
        }
        if(this._attemptsBy.includes(team)) {
            return false;
        }
        this._currentlyAttempting = team;
        this._attemptsBy.push(team);
        this._state = RoundState.TEAM_CAN_ATTEMPT;
        return true;
    }

    public completeAttempt(success: boolean, answerId?: AnswerId): boolean {
        if(this._state !== RoundState.TEAM_CAN_ATTEMPT) {
            return false;
        }
        if(this._currentlyAttempting) {
            if(answerId) {
                this._usedAnswers.push(answerId);
            }
            if(success) {
                this._completedBy = this._currentlyAttempting;
                this._state = RoundState.COMPLETED;
            } else {
                this._state = RoundState.SHOWING_TEXT;
            }
            this._currentlyAttempting = null;
            return true;
        }
        return false;
    }

    public forceComplete() {
        if(this._state !== RoundState.COMPLETED) {
            this._state = RoundState.COMPLETED;
        }
    }

}