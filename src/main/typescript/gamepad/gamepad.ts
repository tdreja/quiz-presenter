export interface GamepadState {
    gamepad: Gamepad,
    buttonsPressed: boolean
    lastChange: Date
}

const gamepads: Array<GamepadState> = [];

window.addEventListener('gamepadconnected', event => {
    gamepads[event.gamepad.index] = {
        gamepad: event.gamepad,
        buttonsPressed: checkButtonsPressed(event.gamepad),
        lastChange: new Date(),
    };
});
window.addEventListener('gamepaddisconnected', event => {
    delete gamepads[event.gamepad.index];
});

function checkAllGamepadStates() {
    if(gamepads.length === 0) {
        return;
    }
    const updates: Array<GamepadState> = [];
    for(let index=0; index < gamepads.length; index++) {
        const oldState = gamepads[index];
        if(oldState) {
            const pressed = checkButtonsPressed(oldState.gamepad);
            if(pressed != oldState.buttonsPressed) {
                const newState: GamepadState = {
                    gamepad: oldState.gamepad,
                    buttonsPressed: pressed,
                    lastChange: new Date()
                }
                updates.push(newState);
                gamepads[index] = newState;
            }
        }
    }

    // TODO Handle updates
    window.requestAnimationFrame(() => checkAllGamepadStates());
}

function checkButtonsPressed(gamepad: Gamepad): boolean {
    for(let btn of gamepad.buttons) {
        if(btn.pressed) {
            return true;
        }
    }
    return false;
}

checkAllGamepadStates();
