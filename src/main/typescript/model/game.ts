import {Team, TeamColor, TeamUpdate, updateTeams} from "./team";
import {Player} from "./player";
import { GameRound, RoundState } from "./round";



export class GameSection {

    private readonly _name: string;
    private readonly _rounds: Array<GameRound>;
    
    public get rounds() : Array<GameRound> {
        return this._rounds;
    }
    
    public get name() : string {
        return this._name;
    }
    
    public constructor(name: string, rounds: Array<GameRound>) {
        this._name = name;
        this._rounds = rounds;
    }
}

export class Game {
    private readonly _sections: Array<GameSection>;
    private _players: Array<Player>;
    private _teams: Array<Team>;
    private _selectingTeam: TeamColor | null;
    private _round: GameRound | null;

    
    public get players() : Array<Player> {
        return this._players;
    }
    
    public get teams() : Array<Team> {
        return this._teams;
    }
    
    public get sections() : Array<GameSection> {
        return this._sections;
    }

    public get round(): GameRound | null {
        return this._round;
    }

    public get selectingTeam() : TeamColor | null {
        return this._selectingTeam;
    }

    public constructor(sections: Array<GameSection>) {
        this._players = [];
        this._teams = [];
        this._sections = sections;
        this._round = null;
        this._selectingTeam = null;
    }

    public startNextRound(sectionName: string, roundPoints: number): boolean {
        // Find the relevant round
        const section = this._sections.find(s => s.name === sectionName);
        if(!section) {
            return false;
        }
        const round = section.rounds.find(r => r.pointsForCompletion === roundPoints);
        if(!round) {
            return false;
        }

        let changes = false;

        // Complete the old round
        if(this._round) {
            changes = true;
            this._round.forceComplete();
        }
        this._round = null;

        // Try to start the new round
        if(round.startRound()) {
            changes = true;
            this._round = round;
        }

        return changes;
    }

    public selectNextRound(): boolean {
        if(this._round) {
            this._round.forceComplete();
            this._round = null;
            return true;
        }
        return false;
    }
}