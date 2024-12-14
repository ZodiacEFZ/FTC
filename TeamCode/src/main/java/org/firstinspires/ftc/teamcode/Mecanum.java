package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Mecanum", group = "Iterative OpMode")
public class Mecanum extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor rear_left;

    private DcMotor rear_right;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        front_left = hardwareMap.get(DcMotor.class, "front_left");
        front_right = hardwareMap.get(DcMotor.class, "front_right");
        rear_left = hardwareMap.get(DcMotor.class, "rear_left");
        rear_right = hardwareMap.get(DcMotor.class, "rear_right");

        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        rear_left.setDirection(DcMotorSimple.Direction.REVERSE);
        rear_right.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double front_left_power = (y + x + rx) / denominator;
        double rear_left_power = (y - x + rx) / denominator;
        double front_right_power = (y - x - rx) / denominator;
        double rear_right_power = (y + x - rx) / denominator;

        front_left.setPower(front_left_power);
        rear_left.setPower(rear_left_power);
        front_right.setPower(front_right_power);
        rear_right.setPower(rear_right_power);

        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("Motors", "FL:(%.2f) FR:(%.2f) RL:(%.2f) RR:(%.2f)", front_left_power,
                front_right_power, rear_left_power, rear_right_power);
    }

    @Override
    public void stop() {
    }
}
