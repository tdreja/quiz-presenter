import { Game } from '../model/game';
import { Emoji, getEmojiCharacter, Player } from '../model/player';
import { Team, TeamColor } from '../model/team';
import {
    addClickListenerToPart,
    getPart,
    updateAttributeAtPart,
    updateFromMap,
    updatePartInnerHtml
} from './render-utils';
import {RequestAttemptEvent} from "../events/round-events";

export function renderTeams(game: Game) {
    const teamsContainer = document.getElementById('teams-container');
    if (!teamsContainer) {
        console.error(
            'Could not render teams! No container with ID teams-container found!'
        );
        return;
    }
    updateFromMap(teamsContainer, 'team', game.teams, (element, color, team, newElement) =>
        updateTeam(game, element as HTMLElement, color, team, newElement)
    );
}

function updateTeam(game: Game, element: HTMLElement, color: TeamColor, team: Team, newElement: boolean) {
    element.style.setProperty('--team-color', `var(--color-${color.toLowerCase()})`);
    element.style.setProperty('--team-order', `${-team.points}`);

    updatePartInnerHtml(element, 'team-name', color);
    updatePartInnerHtml(element, 'team-points', `${team.points}`);

    const players = getPart(element, 'player-list');
    if (players) {
        updateFromMap(
            players as HTMLElement,
            'player',
            team.players,
            (element, emoji, player, newElement) =>
                updatePlayer(element as HTMLElement, emoji, player, newElement)
        );
    }

    const hasGamepad = team.gamepad !== undefined;
    updateAttributeAtPart(element, 'add-controller-btn', 'hidden', hasGamepad ? '' : null);
    updateAttributeAtPart(element, 'remove-controller-btn', 'hidden', hasGamepad ? null : '');

    const canAnswer = game.currentRound && !game.currentRound.alreadyAttempted.has(color);
    updateAttributeAtPart(element, 'answer-btn', 'hidden', canAnswer ? null : '');

    if(newElement) {
        addClickListenerToPart(element, 'answer-btn', () => document.dispatchEvent(new RequestAttemptEvent(color)));
        addClickListenerToPart(element, 'add-controller-btn', ev => console.log('Add Controller', ev.target));
        addClickListenerToPart(element, 'remove-controller-btn', ev => console.log('Remove Controller', ev.target));
    }
}

function updatePlayer(element: HTMLElement, emoji: Emoji, player: Player, newElement: boolean) {
    element.style.setProperty('--player-order', `${-player.points}`);

    updatePartInnerHtml(element, 'emoji-container', getEmojiCharacter(emoji));
    updatePartInnerHtml(element, 'player-name', player.name);
    updatePartInnerHtml(element, 'player-points', `${player.points}`);
}
