import {Player} from "./player";

export enum TeamColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    PURPLE,
    TURQUOISE,
    WHITE
}

export interface Team {
    color: TeamColor,
    points: number,
    players: Array<Player>,
    controller?: number
}