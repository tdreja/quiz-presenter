import {GameEvent} from "./common-events";
import {AddPlayerEventDto, AddTeamEventDto, Color} from "../api/types.gen";

export class AddPlayerEvent extends GameEvent implements AddPlayerEventDto {

    private readonly _playerName;

    public constructor(playerName: string, eventInitDict?: EventInit) {
        super('add-player', eventInitDict);
        this._playerName = playerName;
    }

    public get playerName(): string {
        return this._playerName;
    }

}

export class AddTeamEvent extends GameEvent implements AddTeamEventDto {

    private readonly _targetColor: Color | undefined;

    public constructor(targetColor?: Color, eventInitDict?: EventInit) {
        super('add-team', eventInitDict);
        this._targetColor = targetColor;
    }

    public get targetColor(): Color | undefined {
        return this._targetColor;
    }
}