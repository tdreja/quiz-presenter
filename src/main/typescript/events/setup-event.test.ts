import { Game, GameRound, GameSection, RoundState } from "../model/game";
import { Emoji, Player } from "../model/player";
import { AnswerId, Question } from "../model/question";
import { Team, TeamColor } from "../model/team";
import { AddPlayerEvent, AddTeamEvent, findSmallestTeam, nextRandom, RemovePlayerEvent, RemoveTeamEvent, RenamePlayerEvent, ReRollEmojiEvent } from "./setup-events";

const answerA: AnswerId = 'A';
const answerB: AnswerId = 'B';
const question: Question = {
    questionId: "1",
    questionText: "Test",
    pointsForCompletion: 100
}
let playerBlue: Player;
let playerRed: Player
let teamBlue: Team;
let teamRed: Team;
let round: GameRound;
let section: GameSection;
let game: Game;

beforeEach(() => {
    // Team Blue
    playerBlue = {
        name: 'Duck',
        emoji: Emoji.DUCK,
        points: 0,
        team: TeamColor.BLUE
    }
    teamBlue = {
        color: TeamColor.BLUE,
        points: 0,
        players: new Map()
    }
    teamBlue.players.set(Emoji.DUCK, playerBlue);

    // Team Red
    playerRed = {
        name: 'Camel',
        emoji: Emoji.CAMEL,
        points: 0,
        team: TeamColor.RED
    }
    teamRed = {
        color: TeamColor.RED,
        points: 0,
        players: new Map()
    }
    teamRed.players.set(Emoji.CAMEL, playerRed);

    // Question, Round and Section
    round = {
        question: question,
        state: RoundState.WAIT_ON_REVEAL,
        currentlyAttempting: new Set(),
        completedBy: new Set(),
        alreadyAttempted: new Set(),
        inSection: 'Section'
    }
    section = {
        name: 'Section',
        rounds: [round]
    }

    // Game
    game = {
        sections: [section],
        availableEmojis: new Set(),
        availableColors: new Set(),
        players: new Map(),
        teams: new Map(),
        selectingTeam: null,
        currentRound: null
    }
    game.teams.set(TeamColor.BLUE, teamBlue);
    game.players.set(Emoji.DUCK, playerBlue);
    game.teams.set(TeamColor.RED, teamRed);
    game.players.set(Emoji.CAMEL, playerRed);
});

test('nextRandom', () => {
    const values: Set<string> = new Set();
    expect(nextRandom(values)).toBe(null);


    values.add('First');
    expect(nextRandom(values)).toBe('First');

    values.add('Second');
    let random = nextRandom(values);
    expect(random == 'First' || random === 'Second').toBe(true);
});


test('findSmallestTeam', () => {
    expect(findSmallestTeam(new Map())).toBe(null);

    // Add player2 to team red
    const playerRed2: Player = {
        name: 'CAT',
        emoji: Emoji.CAT,
        points: 0,
        team: TeamColor.RED
    }
    teamRed.players.set(Emoji.CAT, playerRed2);
    expect(findSmallestTeam(game.teams)).toBe(teamBlue);

    // Remove all players
    teamRed.players.clear();
    expect(findSmallestTeam(game.teams)).toBe(teamRed);
});


test('addPlayer', () => {
    const addPlayer = new AddPlayerEvent('CatPlayer');
    expect(game.availableEmojis.size).toBe(0);
    expect(addPlayer.updateGame(game)).toBe(false);
    expect(game.players.size).toBe(2);

    game.availableEmojis.add(Emoji.CAT);
    expect(addPlayer.updateGame(game)).toBe(true);
    expect(game.players.size).toBe(3);
    const cat = game.players.get(Emoji.CAT);
    expect(cat).toBeTruthy();
    const catColor = cat?.team;
    expect(catColor).toBeTruthy();
    const catTeam = catColor ? game.teams.get(catColor) : null;
    expect(catTeam).toBeTruthy();
    expect(catTeam ? catTeam.players.get(Emoji.CAT) : null).toBe(cat);
});

test('removePlayer', () => {
    expect(game.players.size).toBe(2);
    expect(new RemovePlayerEvent(Emoji.DUCK).updateGame(game)).toBe(true);
    expect(game.players.size).toBe(1);
    expect(game.availableEmojis.size).toBe(1);
    expect(game.availableEmojis).toContain(Emoji.DUCK);
    expect(teamBlue.players.size).toBe(0);
});

test('renamePlayer', () => {
    expect(new RenamePlayerEvent(Emoji.CROCODILE, 'Croc').updateGame(game)).toBe(false);

    expect(playerRed.name).toBe('Camel');
    expect(new RenamePlayerEvent(Emoji.CAMEL, 'RedCamel').updateGame(game)).toBe(true);
    expect(playerRed.name).toBe('RedCamel');
});

test('reRollEmoji', () => {
    expect(game.availableEmojis.size).toBe(0);
    expect(new ReRollEmojiEvent(Emoji.DUCK).updateGame(game)).toBe(false);

    game.availableEmojis.add(Emoji.CROCODILE);
    expect(new ReRollEmojiEvent(Emoji.DUCK).updateGame(game)).toBe(true);

    expect(game.players.get(Emoji.DUCK)).toBeUndefined();
    expect(game.players.get(Emoji.CROCODILE)).toBe(playerBlue);
    expect(teamBlue.players.get(Emoji.DUCK)).toBeUndefined();
    expect(teamBlue.players.get(Emoji.CROCODILE)).toBe(playerBlue);
    expect(game.availableEmojis.size).toBe(1);
    expect(game.availableEmojis).toContain(Emoji.DUCK);

    expect(new ReRollEmojiEvent(Emoji.DUCK).updateGame(game)).toBe(false);
});

test('addTeam', () => {
    expect(new AddTeamEvent().updateGame(game)).toBe(false);
    expect(new AddTeamEvent(TeamColor.ORANGE).updateGame(game)).toBe(false);

    game.availableColors.add(TeamColor.ORANGE);
    expect(new AddTeamEvent().updateGame(game)).toBe(true);
    const teamOrange = game.teams.get(TeamColor.ORANGE);
    expect(teamOrange).toBeTruthy();
    expect(teamOrange ? teamOrange.color : null).toBe(TeamColor.ORANGE);
    expect(game.availableColors.size).toBe(0);
    expect(game.teams.size).toBe(3);
});

test('removeTeam', () => {
    expect(new RemoveTeamEvent(TeamColor.ORANGE).updateGame(game)).toBe(false);

    game.availableColors.add(TeamColor.ORANGE);
    expect(new AddTeamEvent(TeamColor.ORANGE).updateGame(game)).toBe(true);
    const teamOrange = game.teams.get(TeamColor.ORANGE);
    expect(teamOrange).toBeTruthy();
    if(!teamOrange) {
        return;
    }
    expect(game.availableColors.size).toBe(0);
    expect(game.teams.size).toBe(3);

    expect(new RemoveTeamEvent(TeamColor.BLUE).updateGame(game)).toBe(true);
    expect(game.availableColors.size).toBe(1);
    expect(game.availableColors).toContain(TeamColor.BLUE);
    expect(game.teams.size).toBe(2);
    expect(game.teams.get(TeamColor.RED)).toBe(teamRed);
    expect(game.teams.get(TeamColor.ORANGE)).toBe(teamOrange);

    expect(teamOrange.players.size).toBe(1);
    expect(teamOrange.players.get(Emoji.DUCK)).toBe(playerBlue);
    expect(playerBlue.team).toBe(TeamColor.ORANGE);
});