import {Player} from "./player";

export enum TeamColor {
    RED = 'RED',
    BLUE = 'BLUE',
    GREEN = 'GREEN',
    YELLOW = 'YELLOW',
    ORANGE = 'ORANGE',
    PURPLE = 'PURPLE',
    TURQUOISE = 'TURQUOISE',
    WHITE = 'WHITE'
}

export interface Team {
    color: TeamColor,
    points: number,
    players: Array<Player>,
    gamepad?: number
}
