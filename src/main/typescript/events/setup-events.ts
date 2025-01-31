import {AddPlayerEventDto, EventType} from '../api/types.gen';


export class AddPlayerEvent extends Event implements AddPlayerEventDto {

    private readonly _playerName: string;

    public constructor(playerName: string) {
        super(EventType.ADD_PLAYER);
        this._playerName = playerName;
    }

    public get playerName(): string {
        return this._playerName;
    }

    public get eventType(): EventType {
        return EventType.ADD_PLAYER;
    }
}
