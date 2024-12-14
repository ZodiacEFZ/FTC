package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.Mecanum;
import org.firstinspires.ftc.teamcode.lib.Xbox;

@TeleOp(name = "Mecanum", group = "Iterative OpMode")
public class MecanumOpMode extends OpMode {
    private final ElapsedTime timer = new ElapsedTime();
    private final Translation2d size = new Translation2d(0.381, 0.381);
    private Mecanum chassis;
    private RevIMU imu;
    private Xbox driver;

    @Override
    public void init() {
        imu = new RevIMU(hardwareMap);
        imu.init();

        chassis = new Mecanum(hardwareMap, size, "frontLeft", "frontRight", "rearLeft", "rearRight",
                Motor.GoBILDA.RPM_435, imu);

        driver = new Xbox(gamepad1).onPressed(GamepadKeys.Button.B, () -> chassis.toggleHeadless());

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
        driver.execute();

        chassis.drive(driver.getLeftX(), driver.getLeftY(), driver.getRightX());

        telemetry.addData("Status", "Time: " + timer);
        telemetry.addData("Speed", "Speed: " + chassis.getWheelSpeeds());
        telemetry.addData("Pose", "Pose: " + chassis.getPose());
    }

    @Override
    public void stop() {
    }
}
