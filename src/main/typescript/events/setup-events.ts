import { Game } from "../model/game";
import { Emoji, Player } from "../model/player";
import { Team, TeamColor } from "../model/team";
import { EventType, GameEvent } from "./common-events";

function nextRandom<VALUE>(values: Set<VALUE>): VALUE | null {
    if(values.size === 0) {
        return null;
    }
    const randNr = Math.floor(Math.random() * values.size);
    let count = 0;
    for(let value of values) {
        if(count === randNr) {
            return value;
        }
        count++;
    }
    return null;
}

function findSmallestTeam(teams: Map<TeamColor, Team>): Team | null {
    let smallestTeam: Team | null = null;
    teams.forEach((team) => {
        if(!smallestTeam || smallestTeam.players.size > team.players.size) {
            smallestTeam = team;
        }
    });
    return smallestTeam;
}

export class AddPlayerEvent extends GameEvent {
  private readonly _name: string;

  public constructor(name: string, eventInitDict?: EventInit) {
    super(EventType.ADD_PLAYER, eventInitDict);
    this._name = name;
  }

  public updateGame(game: Game): boolean {
    const emoji = nextRandom(game.availableEmojis);
    if(!emoji) {
        return false;
    }
    game.availableEmojis.delete(emoji);
    const smallestTeam = findSmallestTeam(game.teams);
    const player: Player = {
        name: this._name,
        emoji: emoji,
        points: 0,
        team: smallestTeam ? smallestTeam.color : null
    };
    game.players.set(emoji, player);
    if(smallestTeam) {
        smallestTeam.players.set(emoji, player);
    }
    return true;
  }
}

export class RemovePlayerEvent extends GameEvent {
    private readonly _emoji: Emoji;
  
    public constructor(emoji: string, eventInitDict?: EventInit) {
      super(EventType.REMOVE_PLAYER, eventInitDict);
      this._emoji = emoji as Emoji;
    }
  
    public updateGame(game: Game): boolean {
        const player = game.players.get(this._emoji);
        if(!player) {
            return false;
        }
        game.players.delete(this._emoji);
        const team = player.team ? game.teams.get(player.team) : null;
        if(team) {
            team.players.delete(this._emoji);
        }
        return true;
    }
  }

  export class AddTeamEvent extends GameEvent {
  
    public constructor(eventInitDict?: EventInit) {
      super(EventType.ADD_TEAM, eventInitDict);
    }
  
    public updateGame(game: Game): boolean {
        const color = nextRandom(game.availableColors);
        if(!color) {
            return false;
        }
        game.availableColors.delete(color);
        const team: Team = {
            color: color,
            points: 0,
            players: new Map()
        };
        game.teams.set(color, team);
        return true;
    }
  }

  export class RemoveTeamEvent extends GameEvent {
  
    private readonly _color: TeamColor;

    public constructor(color: string, eventInitDict?: EventInit) {
      super(EventType.REMOVE_TEAM, eventInitDict);
      this._color = color as TeamColor;
    }
  
    public updateGame(game: Game): boolean {
        const team = game.teams.get(this._color);
        if(!team) {
            return false;
        }
        game.teams.delete(this._color);
        for(let [emoji, player] of team.players) {
            const smallest = findSmallestTeam(game.teams);
            if(smallest) {
                player.team = smallest.color;
                smallest.players.set(emoji, player);
            } else {
                player.team = null;
            }
        }
        return true;
    }

  }