import { Player } from "./player";
import { TeamUpdate } from "./team";

export interface GameUpdate {
    updatedTeams: Array<TeamUpdate>
    updatedPlayers: Array<Player>
}