package org.firstinspires.ftc.teamcode.lib;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.hardware.GyroEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveKinematics;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveOdometry;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveWheelSpeeds;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Mecanum extends SubsystemBase {
    private final ElapsedTime timer = new ElapsedTime();
    private final MotorEx frontLeft, frontRight, rearLeft, rearRight;
    private final GyroEx imu;
    private final MecanumDrive drive;
    private final MecanumDriveOdometry odometry;
    private boolean headless = false;
    private MecanumDriveWheelSpeeds wheelSpeeds;
    private Pose2d pose;

    public Mecanum(HardwareMap hardwareMap, Translation2d size, String frontLeftMotorName, String frontRightMotorName, String rearLeftMotorName, String rearRightMotorName, Motor.GoBILDA goBILDA, GyroEx imu) {
        timer.reset();
        frontLeft = new MotorEx(hardwareMap, frontLeftMotorName, goBILDA);
        frontRight = new MotorEx(hardwareMap, frontRightMotorName, goBILDA);
        rearLeft = new MotorEx(hardwareMap, rearLeftMotorName, goBILDA);
        rearRight = new MotorEx(hardwareMap, rearRightMotorName, goBILDA);
        this.imu = imu;
        drive = new MecanumDrive(frontLeft, frontRight, rearLeft, rearRight);
        MecanumDriveKinematics kinematics = new MecanumDriveKinematics(
                new Translation2d(size.getX(), size.getY()),
                new Translation2d(size.getX(), -size.getY()),
                new Translation2d(-size.getX(), size.getY()),
                new Translation2d(-size.getX(), -size.getY()));
        odometry = new MecanumDriveOdometry(kinematics,
                imu == null ? new Rotation2d() : imu.getRotation2d(), new Pose2d());
    }

    @Override
    public void periodic() {
        wheelSpeeds = new MecanumDriveWheelSpeeds(frontLeft.getVelocity(), frontRight.getVelocity(),
                rearLeft.getVelocity(), rearRight.getVelocity());
        pose = odometry.updateWithTime(timer.seconds(), new Rotation2d(), wheelSpeeds);
    }

    public void drive(double strafe, double forward, double omega) {
        drive.driveFieldCentric(strafe, forward, omega,
                headless ? imu.getRotation2d().getDegrees() : 0);
    }

    public boolean toggleHeadless() {
        return headless = !headless;
    }

    public MecanumDriveWheelSpeeds getWheelSpeeds() {
        return wheelSpeeds;
    }

    public Pose2d getPose() {
        return pose;
    }
}
