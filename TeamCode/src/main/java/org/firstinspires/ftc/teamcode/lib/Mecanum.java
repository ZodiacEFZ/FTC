package org.firstinspires.ftc.teamcode.lib;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.hardware.GyroEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveKinematics;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveOdometry;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveWheelSpeeds;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Mecanum {
    private final ElapsedTime timer = new ElapsedTime();
    private final MotorEx frontLeft, frontRight, rearLeft, rearRight;
    private final GyroEx imu;
    private final MecanumDrive drive;
    private final MecanumDriveOdometry odometry;
    private boolean headless = true;
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

        MecanumDriveKinematics kinematics = getMecanumDriveKinematics(size);
        odometry = new MecanumDriveOdometry(kinematics, imu.getRotation2d(), new Pose2d());
    }

    @NonNull
    private static MecanumDriveKinematics getMecanumDriveKinematics(Translation2d size) {
        Translation2d frontLeftLocation = new Translation2d(size.getX(), size.getY());
        Translation2d frontRightLocation = new Translation2d(size.getX(), -size.getY());
        Translation2d backLeftLocation = new Translation2d(-size.getX(), size.getY());
        Translation2d backRightLocation = new Translation2d(-size.getX(), -size.getY());

        return new MecanumDriveKinematics(frontLeftLocation, frontRightLocation, backLeftLocation,
                backRightLocation);
    }

    public void drive(double strafe, double forward, double omega) {
        drive.driveFieldCentric(strafe, forward, omega,
                headless ? imu.getRotation2d().getDegrees() : 0);
        wheelSpeeds = new MecanumDriveWheelSpeeds(frontLeft.getVelocity(), frontRight.getVelocity(),
                rearLeft.getVelocity(), rearRight.getVelocity());
        pose = odometry.updateWithTime(timer.seconds(), imu.getRotation2d(), wheelSpeeds);
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
