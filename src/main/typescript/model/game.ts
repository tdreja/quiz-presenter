import {Team} from "./team";
import {Player} from "./player";

export interface Game {
    teams: Array<Team>
    allPlayers: Array<Player>
}