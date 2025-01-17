import {Emoji, Player} from "./player";

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

export interface TeamUpdate {
    color: TeamColor,
    points: number,
    players: Array<Emoji>
}


export interface Team {
    color: TeamColor,
    points: number,
    players: Array<Player>,
    gamepad?: number
}

export function updateTeams(teams: Array<Team>, allPlayers: Array<Player>, updates: Array<TeamUpdate>): Array<Team> {
    const result: Array<Team> = [];
    for(const update of updates) {
        const team = teams.find(t => t.color === update.color);
        if(team) {
            team.points = update.points;
            team.players = allPlayers.filter(p => update.players.includes(p.emoji));
            result.push(team);
        } else {
            result.push({
                color: update.color,
                points: update.points,
                players: allPlayers.filter(p => update.players.includes(p.emoji))
            });
        }
    }
    return result;
}