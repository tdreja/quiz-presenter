import { Game, GameRound } from "../model/game";
import { EstimateQuestion, TextMultipleChoiceQuestion } from "../model/question";
import { updateFromMap, updatePart } from "./render-utils";

export function renderQuestion(game: Game) {
    if(!game.currentRound) {
        document.body.removeAttribute('current-question');
        return;
    }

    if(game.currentRound.question instanceof TextMultipleChoiceQuestion) {
        document.body.setAttribute('current-question', 'text-multiple-choice');
        renderTextMultipleChoiceQuestion(game, game.currentRound, game.currentRound.question);
        return;
    }
    if(game.currentRound.question instanceof EstimateQuestion) {
        document.body.setAttribute('current-question', 'estimate');
        renderEstimateQuestion(game, game.currentRound, game.currentRound.question);
        return;
    }
    console.error('Could not render current question. Unknown type!', game.currentRound.question);
}

function renderTextMultipleChoiceQuestion(game: Game, round: GameRound, question: TextMultipleChoiceQuestion) {
    const html = document.getElementById('text-multiple-choice');
    if(!html) {
        console.error('Could not render current question! No html found!', question);
        return;
    }

    updatePart(html, 'section', round.inSection);
    updatePart(html, 'points', `${question.pointsForCompletion}`);
    updatePart(html, 'question-text', question.text);

    const choicesContainer = html.querySelector('[part=answer-choices]');
    if(!choicesContainer) {
        return;
    }

    updateFromMap(choicesContainer, 'choice', question.choices, (element, choiceId, choice) => {
        updatePart(element, 'choice-id', choiceId);
        updatePart(element, 'choice-text', choice.text);
    });
}

function renderEstimateQuestion(game: Game, round: GameRound, question: EstimateQuestion) {

}