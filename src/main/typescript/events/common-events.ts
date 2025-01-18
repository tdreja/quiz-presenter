import { Game } from "../model/game";

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