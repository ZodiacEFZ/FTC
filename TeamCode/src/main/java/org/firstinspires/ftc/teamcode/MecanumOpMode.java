package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.Mecanum;
import org.firstinspires.ftc.teamcode.lib.REVIMU;
import org.firstinspires.ftc.teamcode.lib.Xbox;

@TeleOp(name = "Mecanum", group = "Iterative OpMode")
public class MecanumOpMode extends OpMode {
    private final ElapsedTime timer = new ElapsedTime();
    private final Translation2d size = new Translation2d(0.1675, 0.2075);
    private Mecanum chassis;
    private Xbox driver;

    @Override
    public void init() {
        REVIMU imu = new REVIMU(hardwareMap);

        chassis = new Mecanum(hardwareMap, size, "frontLeft", "frontRight", "rearLeft", "rearRight",
                Motor.GoBILDA.RPM_312, imu);

        driver = new Xbox(gamepad1).onPressed(GamepadKeys.Button.B, () -> {
            if (chassis.toggleHeadless()) {
                driver.rumble();
            }
        });

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        chassis.drive(-driver.getLeftX(), driver.getLeftY(), -driver.getRightX());

        telemetry.addData("Time", timer);
        telemetry.addData("Speed", chassis.getWheelSpeeds());
        telemetry.addData("Pose", chassis.getPose());
    }

    @Override
    public void stop() {
    }
}
