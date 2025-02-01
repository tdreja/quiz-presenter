import {EventType, GameEvent} from "./common-events";
import {
    AddPlayerEventDto,
    AddTeamEventDto, AssignGamepadEventDto,
    Color,
    Emoji,
    RemovePlayerEventDto,
    RemoveTeamEventDto,
    RenamePlayerEventDto, RequestGamepadEventDto,
    ReRollPlayerEmojiEventDto,
    ShuffleTeamsEventDto
} from "../api/types.gen";

export class AddPlayerEvent extends GameEvent implements AddPlayerEventDto {

    private readonly _playerName;

    public constructor(playerName: string, eventInitDict?: EventInit) {
        super(EventType.ADD_PLAYER, eventInitDict);
        this._playerName = playerName;
    }

    public get playerName(): string {
        return this._playerName;
    }

}

export class RemovePlayerEvent extends GameEvent implements RemovePlayerEventDto {
    private readonly _playerEmoji: Emoji;

    public constructor(playerEmoji: Emoji, eventInitDict?: EventInit) {
        super(EventType.REMOVE_PLAYER, eventInitDict);
        this._playerEmoji = playerEmoji;
    }

    public get playerEmoji(): Emoji {
        return this._playerEmoji;
    }
}

export class RenamePlayerEvent extends GameEvent implements RenamePlayerEventDto {
    private readonly _newName: string;
    private readonly _playerEmoji: Emoji;

    public constructor(playerEmoji: Emoji, newName: string, eventInitDict?: EventInit) {
        super(EventType.RENAME_PLAYER, eventInitDict);
        this._newName = newName;
        this._playerEmoji = playerEmoji;
    }

    public get newName(): string {
        return this._newName;
    }

    public get playerEmoji(): Emoji {
        return this._playerEmoji;
    }
}

export class ReRollPlayerEmojiEvent extends GameEvent implements ReRollPlayerEmojiEventDto {
    private readonly _emoji: Emoji;

    public constructor(emoji: Emoji, eventInitDict?: EventInit) {
        super(EventType.RE_ROLL_PLAYER_EMOJI, eventInitDict);
        this._emoji = emoji;
    }

    public get currentEmoji(): Emoji {
        return this._emoji;
    }
}

export class AddTeamEvent extends GameEvent implements AddTeamEventDto {

    private readonly _targetColor: Color | undefined;

    public constructor(targetColor?: Color, eventInitDict?: EventInit) {
        super(EventType.ADD_TEAM, eventInitDict);
        this._targetColor = targetColor;
    }

    public get targetColor(): Color | undefined {
        return this._targetColor;
    }
}

export class AssignGamepadEvent extends GameEvent implements AssignGamepadEventDto {
    private readonly _teamColor: Color;
    private readonly _gamepadId: string | undefined;

    public constructor(color: Color, gamepadId?: string, eventInitDict?: EventInit) {
        super(EventType.ASSIGN_CONTROLLER, eventInitDict);
        this._teamColor = color;
        this._gamepadId = gamepadId;
    }

    public get teamColor(): Color {
        return this._teamColor;
    }

    public get gamepadId(): string | undefined {
        return this._gamepadId;
    }
}

export class RemoveTeamEvent extends GameEvent implements RemoveTeamEventDto {
    private readonly _teamColor: Color;

    public constructor(color: Color, eventInitDict?: EventInit) {
        super(EventType.REMOVE_TEAM, eventInitDict);
        this._teamColor = color;
    }

    public get teamColor(): Color {
        return this._teamColor;
    }
}

export class RequestGamepadEvent extends GameEvent implements RequestGamepadEventDto {
    private readonly _teamColor: Color;

    public constructor(color: Color, eventInitDict?: EventInit) {
        super(EventType.REQUEST_CONTROLLER, eventInitDict);
        this._teamColor = color;
    }

    public get teamColor(): Color {
        return this._teamColor;
    }
}

export class ShuffleTeamsEvent extends GameEvent implements ShuffleTeamsEventDto {
    private readonly _newTeams: Array<Color>;

    public constructor(teams: Array<Color>, eventInitDict?: EventInit) {
        super(EventType.SHUFFLE_TEAMS, eventInitDict);
        this._newTeams = teams;
    }

    public get newTeams(): Array<Color> {
        return this._newTeams;
    }
}