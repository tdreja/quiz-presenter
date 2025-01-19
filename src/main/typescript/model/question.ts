export interface Question {
    questionId: string,
    questionText: string,
    pointsForCompletion: number
}

export type AnswerId = string;

export interface MultipleChoiceQuestion extends Question {
    answerOptions: Array<AnswerOption>;
}

export interface AnswerOption {
    answerId: AnswerId,
    answerText: string,
    correct: boolean
}