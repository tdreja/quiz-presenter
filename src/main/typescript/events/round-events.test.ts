import { Game, GameRound, GameSection, RoundState } from "../model/game";
import { Emoji, Player } from "../model/player";
import { AnswerId, Question } from "../model/question";
import { Team, TeamColor } from "../model/team";
import { ActivateBuzzerEvent, RequestAttemptEvent, StartRoundEvent } from "./round-events";

const startRound = new StartRoundEvent('Section', 'CatA_100');
const activateBuzzer = new ActivateBuzzerEvent();
const requestAttemptBlue = new RequestAttemptEvent(TeamColor.BLUE);
const requestAttemptRed = new RequestAttemptEvent(TeamColor.RED);

const question: Question = {
    questionId: "CatA_100",
    questionText: "Test",
    pointsForCompletion: 100
}
let playerBlue: Player;
let playerRed: Player;
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


test('startRound', () => {
    expect(startRound.updateGame(game)).toBe(true);
    expect(game.currentRound).toBe(round);
    expect(round.state).toBe(RoundState.SHOWING_TEXT);
    expect(startRound.updateGame(game)).toBe(false);

    // Ignore invalid IDs
    expect(new StartRoundEvent('', '0').updateGame(game)).toBe(false);
});

test('activateBuzzer', () => {
    expect(activateBuzzer.updateGame(game)).toBe(false);

    // Start a round, then activate buzzer
    expect(startRound.updateGame(game)).toBe(true);
    expect(activateBuzzer.updateGame(game)).toBe(true);
    expect(round.state).toBe(RoundState.BUZZER_ACTIVE);

    // Only once!
    expect(activateBuzzer.updateGame(game)).toBe(false);

    // Not during attempt
    round.state = RoundState.TEAM_CAN_ATTEMPT;
    expect(activateBuzzer.updateGame(game)).toBe(false);

    // Not for completed ones
    round.state = RoundState.COMPLETED;
    expect(activateBuzzer.updateGame(game)).toBe(false);
});

test('requestAttempt', () => {
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(false);

    // Start a new round
    expect(startRound.updateGame(game)).toBe(true);
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(false);

    // Activate Buzzer
    expect(activateBuzzer.updateGame(game)).toBe(true);
    expect(requestAttemptBlue.updateGame(game)).toBe(true);
    expect(round.state).toBe(RoundState.TEAM_CAN_ATTEMPT);
    expect(round.currentlyAttempting).toContain(TeamColor.BLUE);
    expect(round.currentlyAttempting.size).toBe(1);
    expect(round.alreadyAttempted).toContain(TeamColor.BLUE);
    expect(round.alreadyAttempted.size).toBe(1);

    // Not during attempt
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(false);

    // Only one attempt!
    round.state = RoundState.BUZZER_ACTIVE;
    expect(requestAttemptBlue.updateGame(game)).toBe(false);

    // Red still can
    expect(requestAttemptRed.updateGame(game)).toBe(true);
    expect(round.state).toBe(RoundState.TEAM_CAN_ATTEMPT);
    expect(round.currentlyAttempting).toContain(TeamColor.RED);
    expect(round.currentlyAttempting.size).toBe(1);
    expect(round.alreadyAttempted).toContain(TeamColor.BLUE);
    expect(round.alreadyAttempted).toContain(TeamColor.RED);
    expect(round.alreadyAttempted.size).toBe(2);

    // Now no team can
    round.state = RoundState.BUZZER_ACTIVE;
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(false);
});