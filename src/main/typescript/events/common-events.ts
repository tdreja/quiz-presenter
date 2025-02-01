import {GameEventDto} from "../api/types.gen";

export type EventType = 'add-player' | 'add-team';

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