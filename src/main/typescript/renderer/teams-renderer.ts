import { Game } from '../model/game';
import { Emoji, getEmojiCharacter, Player } from '../model/player';
import { Team, TeamColor } from '../model/team';
import { updateFromMap, updatePart } from './render-utils';

export function renderTeams(game: Game) {
    const teamsContainer = document.getElementById('teams-container');
    if (!teamsContainer) {
        console.error(
            'Could not render teams! No container with ID teams-container found!'
        );
        return;
    }
    updateFromMap(teamsContainer, 'team', game.teams, (element, color, team) =>
        updateTeam(element as HTMLElement, color, team)
    );
}

function updateTeam(element: HTMLElement, color: TeamColor, team: Team) {
    element.style.setProperty('--team-color', `var(--color-${color.toLowerCase()})`);
    element.style.setProperty('--team-order', `${-team.points}`);

    const teamName = element.querySelector('[part=team-name]');
    if (teamName) {
        teamName.innerHTML = color;
    }

    const points = element.querySelector('[part=team-points]');
    if (points) {
        points.innerHTML = `${team.points}`;
    }

    const players = element.querySelector('[part=player-list]');
    if (players) {
        updateFromMap(
            players as HTMLElement,
            'player',
            team.players,
            (element, emoji, player) =>
                updatePlayer(element as HTMLElement, emoji, player)
        );
    }
}

function updatePlayer(element: HTMLElement, emoji: Emoji, player: Player) {
    element.style.setProperty('--player-order', `${-player.points}`);

    updatePart(element, 'emoji-container', getEmojiCharacter(emoji));
    updatePart(element, 'player-name', player.name);
    updatePart(element, 'player-points', `${player.points}`);
}
