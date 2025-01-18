import { Game, GameRound, GameSection, RoundState } from "../model/game";
import { Emoji, Player } from "../model/player";
import { AnswerId, Question } from "../model/question";
import { Team, TeamColor } from "../model/team";
import { ActivateBuzzerEvent, CompleteAttemptEvent, RequestAttemptEvent, StartRoundEvent } from "./round-events";

const answerA: AnswerId = 'A';
const answerB: AnswerId = 'B';
const startRound = new StartRoundEvent('Section', '100');
const activateBuzzer = new ActivateBuzzerEvent();
const requestAttemptBlue = new RequestAttemptEvent(TeamColor.BLUE);
const requestAttemptRed = new RequestAttemptEvent(TeamColor.RED);
const completeAttemptSuccessful = new CompleteAttemptEvent(true, answerB);
const completeAttemptFailure = new CompleteAttemptEvent(false, answerA);

const question: Question = {
    questionId: "1",
    questionText: "Test"
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
        pointsForCompletion: 100,
        question: question,
        state: RoundState.WAIT_ON_REVEAL,
        currentlyAttempting: null,
        completedBy: null,
        inSection: 'Section',
        attemptsBy: [],
        usedAnswers: []
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
    expect(round.currentlyAttempting).toBe(teamBlue);
    expect(round.attemptsBy).toContain(teamBlue);
    expect(round.attemptsBy).toHaveLength(1);

    // Not during attempt
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(false);

    // Only one attempt!
    round.state = RoundState.BUZZER_ACTIVE;
    expect(requestAttemptBlue.updateGame(game)).toBe(false);

    // Red still can
    expect(requestAttemptRed.updateGame(game)).toBe(true);
    expect(round.state).toBe(RoundState.TEAM_CAN_ATTEMPT);
    expect(round.currentlyAttempting).toBe(teamRed);
    expect(round.attemptsBy).toContain(teamBlue);
    expect(round.attemptsBy).toContain(teamRed);
    expect(round.attemptsBy).toHaveLength(2);

    // Now no team can
    round.state = RoundState.BUZZER_ACTIVE;
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(false);
});

test('completeAttempt', () => {
    expect(completeAttemptFailure.updateGame(game)).toBe(false);
    expect(completeAttemptSuccessful.updateGame(game)).toBe(false);
    expect(round.attemptsBy).toHaveLength(0);

    // Start a new round, Activate Buzzer & Attempt Blue
    expect(startRound.updateGame(game)).toBe(true);
    expect(activateBuzzer.updateGame(game)).toBe(true);
    expect(requestAttemptBlue.updateGame(game)).toBe(true);
    expect(round.currentlyAttempting).toBe(teamBlue);
    expect(round.attemptsBy).toContain(teamBlue);
    expect(round.attemptsBy).toHaveLength(1);

    // Blue ends in failure
    expect(completeAttemptFailure.updateGame(game)).toBe(true);
    expect(round.attemptsBy).toContain(teamBlue);
    expect(round.attemptsBy).toHaveLength(1);
    expect(round.currentlyAttempting).toBeNull();
    expect(round.completedBy).toBeNull();
    expect(round.usedAnswers).toContain(answerA);
    expect(round.state).toBe(RoundState.SHOWING_TEXT);

    // Activate Buzzer and go again with red successfully
    expect(activateBuzzer.updateGame(game)).toBe(true);
    expect(requestAttemptBlue.updateGame(game)).toBe(false);
    expect(requestAttemptRed.updateGame(game)).toBe(true);
    expect(completeAttemptSuccessful.updateGame(game)).toBe(true);

    // Both teams attempted, red completed the round
    expect(round.attemptsBy).toContain(teamBlue);
    expect(round.attemptsBy).toContain(teamRed);
    expect(round.attemptsBy).toHaveLength(2);
    expect(round.currentlyAttempting).toBeNull();
    expect(round.completedBy).toBe(teamRed);

    // Both answers were used, the round is complete
    expect(round.usedAnswers).toContain(answerA);
    expect(round.usedAnswers).toContain(answerB);
    expect(round.usedAnswers).toHaveLength(2);
    expect(round.state).toBe(RoundState.COMPLETED);

    // Game needs a new round
    expect(game.currentRound).toBeNull();

    // Team Red and Player Red have more points
    expect(teamRed.points).toBe(100);
    expect(playerRed.points).toBe(100);
    expect(teamBlue.points).toBe(0);
    expect(playerBlue.points).toBe(0);
});

test('completeAttemptNobody', () => {
    expect(startRound.updateGame(game)).toBe(true);
    expect(round.state).toBe(RoundState.SHOWING_TEXT);

    // Mark question and move on
    expect(activateBuzzer.updateGame(game)).toBe(true);
    expect(completeAttemptFailure.updateGame(game)).toBe(true);
    expect(round.state).toBe(RoundState.SHOWING_TEXT);
    expect(round.usedAnswers).toContain(answerA);
    expect(round.usedAnswers).toHaveLength(1);

    // Event completed by nobody
    expect(activateBuzzer.updateGame(game)).toBe(true);
    expect(completeAttemptSuccessful.updateGame(game)).toBe(true);

    // Both answers were used, the round is complete
    expect(round.state).toBe(RoundState.COMPLETED);
    expect(round.usedAnswers).toContain(answerA);
    expect(round.usedAnswers).toContain(answerB);
    expect(round.usedAnswers).toHaveLength(2);

    // Game needs a new round
    expect(game.currentRound).toBeNull();

    // Nobody won points
    expect(teamRed.points).toBe(0);
    expect(playerRed.points).toBe(0);
    expect(teamBlue.points).toBe(0);
    expect(playerBlue.points).toBe(0);
});