import {Team, TeamColor, TeamUpdate, updateTeams} from "./team";
import {Emoji, Player} from "./player";
import { GameRound, RoundState } from "./round";
import { Options } from "./options";



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
    private readonly _emojis: Options<Emoji>;
    private readonly _colors: Options<TeamColor>;
    private _players: Array<Player>;
    private _teams: Array<Team>;
    private _selectingTeam: Team | null;
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

    public get selectingTeam() : Team | null {
        return this._selectingTeam;
    }

    public constructor(sections: Array<GameSection>) {
        this._emojis = new Options(Object.keys(Emoji).map(key => key as unknown as Emoji));
        this._colors = new Options(Object.keys(TeamColor).map(key => key as unknown as TeamColor));
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