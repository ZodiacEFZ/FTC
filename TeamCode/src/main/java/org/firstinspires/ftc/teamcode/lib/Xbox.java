package org.firstinspires.ftc.teamcode.lib;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.ButtonReader;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Xbox extends SubsystemBase {
    private final HashMap<Button, Set<Runnable>> onChangeActions = new HashMap<>();
    private final HashMap<Button, Set<Runnable>> onDownActions = new HashMap<>();
    private final HashMap<Button, Set<Runnable>> onPressedActions = new HashMap<>();
    private final HashMap<Button, Set<Runnable>> onReleasedActions = new HashMap<>();
    private final GamepadEx gamepad;

    public Xbox(Gamepad gamepad) {
        this.gamepad = new GamepadEx(gamepad);
    }

    @Override
    public void periodic() {
        onChangeActions.forEach(((button, runnables) -> {
            if (gamepad.stateJustChanged(button)) {
                runnables.forEach(Runnable::run);
            }
        }));
        onDownActions.forEach(((button, runnables) -> {
            if (gamepad.isDown(button)) {
                runnables.forEach(Runnable::run);
            }
        }));
        onPressedActions.forEach(((button, runnables) -> {
            if (gamepad.wasJustPressed(button)) {
                runnables.forEach(Runnable::run);
            }
        }));
        onReleasedActions.forEach(((button, runnables) -> {
            if (gamepad.wasJustReleased(button)) {
                runnables.forEach(Runnable::run);
            }
        }));
    }

    public void rumble() {
        rumble(0.5, 0.5);
    }

    public void rumble(double power, double seconds) {
        gamepad.gamepad.rumble(power, power, (int) (seconds * 1000));
    }

    public Xbox onChange(Button button, Runnable func) {
        Set<Runnable> runnables = onChangeActions.get(button);
        if (runnables == null) {
            runnables = new HashSet<>();
        }
        runnables.add(func);
        onChangeActions.put(button, runnables);
        return this;
    }

    public Xbox onDown(Button button, Runnable func) {
        Set<Runnable> runnables = onDownActions.get(button);
        if (runnables == null) {
            runnables = new HashSet<>();
        }
        runnables.add(func);
        onDownActions.put(button, runnables);
        return this;
    }

    public Xbox onPressed(Button button, Runnable func) {
        Set<Runnable> runnables = onPressedActions.get(button);
        if (runnables == null) {
            runnables = new HashSet<>();
        }
        runnables.add(func);
        onPressedActions.put(button, runnables);
        return this;
    }

    public Xbox onReleased(Button button, Runnable func) {
        Set<Runnable> runnables = onReleasedActions.get(button);
        if (runnables == null) {
            runnables = new HashSet<>();
        }
        runnables.add(func);
        onReleasedActions.put(button, runnables);
        return this;
    }

    /**
     * @param button the button object
     * @return the boolean value as to whether the button is active or not
     */
    public boolean getButton(Button button) {
        return gamepad.getButton(button);
    }

    /**
     * @param trigger the trigger object
     * @return the value returned by the trigger in question
     */
    public double getTrigger(GamepadKeys.Trigger trigger) {
        return gamepad.getTrigger(trigger);
    }

    /**
     * @return the y-value on the left analog stick
     */
    public double getLeftY() {
        return gamepad.getLeftY();
    }

    /**
     * @return the y-value on the right analog stick
     */
    public double getRightY() {
        return gamepad.getRightY();
    }

    /**
     * @return the x-value on the left analog stick
     */
    public double getLeftX() {
        return gamepad.getLeftX();
    }

    /**
     * @return the x-value on the right analog stick
     */
    public double getRightX() {
        return gamepad.getRightX();
    }

    /**
     * Returns if the button was just pressed
     *
     * @param button the desired button to read from
     * @return if the button was just pressed
     */
    public boolean wasJustPressed(Button button) {
        return gamepad.wasJustPressed(button);
    }

    /**
     * Returns if the button was just released
     *
     * @param button the desired button to read from
     * @return if the button was just released
     */
    public boolean wasJustReleased(Button button) {
        return gamepad.wasJustReleased(button);
    }

    /**
     * Updates the value for each {@link ButtonReader}.
     * Call this once in your loop.
     */
    public void readButtons() {
        gamepad.readButtons();
    }

    /**
     * Returns if the button is down
     *
     * @param button the desired button to read from
     * @return if the button is down
     */
    public boolean isDown(Button button) {
        return gamepad.isDown(button);
    }

    /**
     * Returns if the button's state has just changed
     *
     * @param button the desired button to read from
     * @return if the button's state has just changed
     */
    public boolean stateJustChanged(Button button) {
        return gamepad.stateJustChanged(button);
    }

    /**
     * @param button the matching button key to the gamepad button
     * @return the commandable button
     */
    public GamepadButton getGamepadButton(Button button) {
        return gamepad.getGamepadButton(button);
    }

}
