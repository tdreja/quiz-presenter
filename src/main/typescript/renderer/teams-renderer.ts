import { Game } from "../model/game";
import { Team } from "../model/team";

export function renderTeams(game: Game) {
    const teamsContainer = document.getElementById('teams-container');
    if(!teamsContainer) {
        console.error('Could not render teams! No container with ID teams-container found!');
        return;
    }

    for(let [_, team] of game.teams) {
        renderTeam(game, teamsContainer, team);
    }
}

function renderTeam(game: Game, teamContainer: HTMLElement, team: Team) {
    // Reuse or create the team DIV
    let container: HTMLElement | null = teamContainer.querySelector(`[team=${team.color}]`);
    if(!container) {
        container = document.createElement('div');
        container.setAttribute('team', team.color);
        container.style.setProperty('--team-color', team.color);
        teamContainer.append(container);
    }

    container.innerText = `Team ${team.color}: ${team.points}`;
}