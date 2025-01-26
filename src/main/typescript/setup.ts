import { Game } from "./model/game";
import { Emoji, Player } from "./model/player";
import { Team, TeamColor } from "./model/team";
import { renderTeams } from "./renderer/teams-renderer";

export const game: Game = {
    sections: [],
    availableEmojis: new Set(),
    availableColors: new Set(),
    players: new Map(),
    teams: new Map(),
    selectingTeam: null,
    currentRound: null,
    roundCounter: 0
}

// region Teams & Players

export const playerBlueDuck: Player = {
    name: "Duck",
    emoji: Emoji.DUCK,
    points: 25,
    team: TeamColor.BLUE
}
export const playerBlueCamel: Player = {
    name: "Camel",
    emoji: Emoji.CAMEL,
    points: 20,
    team: TeamColor.BLUE
}
export const teamBlue: Team = {
    color: TeamColor.BLUE,
    points: 20,
    players: new Map()
}
teamBlue.players.set(Emoji.DUCK, playerBlueDuck);
teamBlue.players.set(Emoji.CAMEL, playerBlueCamel);
game.players.set(Emoji.DUCK, playerBlueDuck);
game.players.set(Emoji.CAMEL, playerBlueCamel);
game.teams.set(TeamColor.BLUE, teamBlue);
game.selectingTeam = teamBlue;

export const playerRedCat: Player = {
    name: "Cat",
    emoji: Emoji.CAT,
    points: 80,
    team: TeamColor.RED
}
export const playerRedEagle: Player = {
    name: "Eagle",
    emoji: Emoji.EAGLE,
    points: 190,
    team: TeamColor.RED
}
export const teamRed: Team = {
    color: TeamColor.RED,
    points: 100,
    players: new Map()
}
teamRed.players.set(Emoji.CAT, playerRedCat);
teamRed.players.set(Emoji.EAGLE, playerRedEagle);
game.players.set(Emoji.CAT, playerRedCat);
game.players.set(Emoji.EAGLE, playerRedEagle);
game.teams.set(TeamColor.RED, teamRed);

// endregion Teams & Players