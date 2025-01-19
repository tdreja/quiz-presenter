import { Game, GameRound } from "../model/game";
import { EstimateQuestion, TextMultipleChoiceQuestion } from "../model/question";

export function renderQuestion(game: Game) {
    const questionContainer = document.getElementById('question-container');
    if(!questionContainer) {
        console.error('Could not render question! No container with ID question-container found!');
        return;
    }
    if(!game.currentRound) {
        questionContainer.style.display = 'none';
        return;
    }

    if(game.currentRound.question instanceof TextMultipleChoiceQuestion) {
        renderTextMultipleChoiceQuestion(game, questionContainer, game.currentRound, game.currentRound.question);
        return;
    }
    if(game.currentRound.question instanceof EstimateQuestion) {
        renderEstimateQuestion(game, questionContainer, game.currentRound, game.currentRound.question);
        return;
    }
    console.error('Could not render current question. Unknown type!', game.currentRound.question);
}

function renderTextMultipleChoiceQuestion(game: Game, questionContainer: HTMLElement, round: GameRound, question: TextMultipleChoiceQuestion) {

}

function renderEstimateQuestion(game: Game, questionContainer: HTMLElement, round: GameRound, question: EstimateQuestion) {

}