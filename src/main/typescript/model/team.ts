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
    forTeam: TeamColor,
    newPoints: number,
    updatedPlayers: Array<Emoji>
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
        const team = teams.find(t => t.color === update.forTeam);
        if(team) {
            team.points = update.newPoints;
            team.players = allPlayers.filter(p => update.updatedPlayers.includes(p.emoji));
            result.push(team);
        } else {
            result.push({
                color: update.forTeam,
                points: update.newPoints,
                players: allPlayers.filter(p => update.updatedPlayers.includes(p.emoji))
            });
        }
    }
    return result;
}