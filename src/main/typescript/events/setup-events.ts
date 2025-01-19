import { Game } from "../model/game";
import { Emoji, Player } from "../model/player";
import { Team, TeamColor } from "../model/team";
import { EventType, GameEvent } from "./common-events";

export function nextRandom<VALUE>(values: Set<VALUE>): VALUE | null {
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

export function findSmallestTeam(teams: Map<TeamColor, Team>): Team | null {
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
        game.availableEmojis.add(this._emoji);
        const team = player.team ? game.teams.get(player.team) : null;
        if(team) {
            team.players.delete(this._emoji);
        }
        return true;
    }
  }

  export class RenamePlayerEvent extends GameEvent {
    private readonly _emoji: Emoji;
    private readonly _newName: string;
  
    public constructor(emoji: string, newName: string, eventInitDict?: EventInit) {
      super(EventType.RENAME_PLAYER, eventInitDict);
      this._emoji = emoji as Emoji;
      this._newName = newName;
    }
  
    public updateGame(game: Game): boolean {
        const player = game.players.get(this._emoji);
        if(!player) {
            return false;
        }
        player.name = this._newName;
        return true;
    }
  }

  export class ReRollEmojiEvent extends GameEvent {
    private readonly _oldEmoji: Emoji;
  
    public constructor(oldEmoji: string, eventInitDict?: EventInit) {
      super(EventType.RENAME_PLAYER, eventInitDict);
      this._oldEmoji = oldEmoji as Emoji;
    }
  
    public updateGame(game: Game): boolean {
        const player = game.players.get(this._oldEmoji);
        if(!player) {
            return false;
        }
        // Find new available emoji
        const newEmoji = nextRandom(game.availableEmojis);
        if(!newEmoji) {
            return false;
        }
        game.availableEmojis.delete(newEmoji);
        game.availableEmojis.add(this._oldEmoji);

        // Update player and teams
        player.emoji = newEmoji;
        game.players.delete(this._oldEmoji);
        game.players.set(newEmoji, player);
        const team = player.team ? game.teams.get(player.team) : null;
        if(team) {
            team.players.delete(this._oldEmoji);
            team.players.set(newEmoji, player);
        }
        return true;
    }
  }

  export class AddTeamEvent extends GameEvent {
  
    private readonly _color?: string | null;

    public constructor(color?: string | null, eventInitDict?: EventInit) {
      super(EventType.ADD_TEAM, eventInitDict);
      this._color = color;
    }
  
    public updateGame(game: Game): boolean {
        let newColor: TeamColor | null = this._color ? this._color as TeamColor : null;
        if(!newColor || !game.availableColors.has(newColor)) {
            newColor = nextRandom(game.availableColors);
        }
        if(!newColor) {
            return false;
        }
        game.availableColors.delete(newColor);
        const team: Team = {
            color: newColor,
            points: 0,
            players: new Map()
        };
        game.teams.set(newColor, team);
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
        game.availableColors.add(this._color);

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