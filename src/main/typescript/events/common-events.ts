import { Game } from "../model/game";

export enum EventType {
    // Events for each round
    START_ROUND = 'start-round',
    ACTIVATE_BUZZER = 'activate-buzzer',
    REQUEST_ATTEMPT = 'request-attempt',
    COMPLETE_ATTEMPT = 'complete-attempt',

    // Events to setup the game
    ADD_PLAYER = 'add-player',
    REMOVE_PLAYER = 'remove-player',
    ADD_TEAM = 'add-team',
    REMOVE_TEAM = 'remove-team'
}

export abstract class GameEvent extends Event {

    protected constructor(type: EventType, eventInitDict?: EventInit) {
        super(type, eventInitDict);
    }

    public abstract updateGame(game: Game): boolean
}