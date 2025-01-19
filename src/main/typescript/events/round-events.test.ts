import { RoundState } from "../model/game";
import { TeamColor } from "../model/team";
import { game, newTestSetup, questionId, round, sectionId } from "./data.test";
import {
  ActivateBuzzerEvent,
  RequestAttemptEvent,
  StartRoundEvent,
} from "./round-events";

const startRound = new StartRoundEvent(sectionId, questionId);
const activateBuzzer = new ActivateBuzzerEvent();
const requestAttemptBlue = new RequestAttemptEvent(TeamColor.BLUE);
const requestAttemptRed = new RequestAttemptEvent(TeamColor.RED);

beforeEach(() => {
  newTestSetup();
});

test("startRound", () => {
  expect(startRound.updateGame(game)).toBe(true);
  expect(game.currentRound).toBe(round);
  expect(round.state).toBe(RoundState.SHOWING_TEXT);
  expect(startRound.updateGame(game)).toBe(false);

  // Ignore invalid IDs
  expect(new StartRoundEvent("", "0").updateGame(game)).toBe(false);
});

test("activateBuzzer", () => {
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

test("requestAttempt", () => {
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
