package org.firstinspires.ftc.teamcode.lib;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Xbox extends GamepadEx {
    private final HashMap<Button, Set<Runnable>> onPressedActions = new HashMap<>();

    public Xbox(Gamepad gamepad) {
        super(gamepad);
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

    public void execute() {
        onPressedActions.forEach(((button, runnables) -> {
            if (wasJustPressed(button)) {
                runnables.forEach(Runnable::run);
            }
        }));
    }
}
