import {GameEventDto} from "../api/types.gen";

export enum EventType {
    // Player
    ADD_PLAYER = 'add-player',
    REMOVE_PLAYER = 'remove-player',
    RENAME_PLAYER = 'rename-player',
    RE_ROLL_PLAYER_EMOJI = 're-roll-player-emoji',
    // Teams
    ADD_TEAM = 'add-team',
    REMOVE_TEAM = 'remove-team',
    SHUFFLE_TEAMS = 'shuffle-teams',
    ASSIGN_CONTROLLER = 'assign-controller',
    REQUEST_CONTROLLER = 'request-controller',
}

export abstract class GameEvent extends Event implements GameEventDto {

    protected constructor(type: EventType, eventInitDict?: EventInit) {
        super(type, eventInitDict);
    }

    public get type(): EventType {
        return super.type as EventType;
    }
}

export abstract class GameRoundEvent extends GameEvent {

    protected constructor(type: EventType, eventInitDict?: EventInit) {
        super(type, eventInitDict);
    }

}