import {alterPlayerPoints, Player} from "./player";

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

export function alterTeamPoints(team: Team, points: number) {
    team.points = team.points + points;
    for(let player of team.players) {
        alterPlayerPoints(player, points);
    }
}