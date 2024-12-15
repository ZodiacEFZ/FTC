package org.firstinspires.ftc.teamcode.lib;

import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.hardware.GyroEx;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class REVIMU extends GyroEx {
    private final BHI260IMU imu;
    /***
     * Heading relative to starting position
     */
    double globalHeading;
    /**
     * Heading relative to last offset
     */
    double relativeHeading;
    /**
     * Offset between global heading and relative heading
     */
    double offset;
    private int multiplier;

    /**
     * Create a new object for the built-in gyro/imu in the REV Expansion Hub with the default configuration name of "imu"
     *
     * @param hw Hardware map
     */
    public REVIMU(HardwareMap hw) {
        this(hw, "imu");
    }

    /**
     * Create a new object for the built-in gyro/imu in the REV Expansion Hub
     *
     * @param hw      Hardware map
     * @param imuName Name of sensor in configuration
     */
    public REVIMU(HardwareMap hw, String imuName) {
        imu = hw.get(BHI260IMU.class, imuName);
        multiplier = 1;
    }

    /**
     * Initializes gyro with default parameters.
     */
    public void init() {
        init(new BHI260IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
    }

    /**
     * Initializes gyro with custom parameters.
     */
    public void init(BHI260IMU.Parameters parameters) {
        imu.initialize(parameters);

        globalHeading = 0;
        relativeHeading = 0;
        offset = 0;
    }

    /**
     * @return Relative heading of the robot
     */
    public double getHeading() {
        // Return yaw
        return getAbsoluteHeading() - offset;
    }

    /**
     * @return Absolute heading of the robot
     */
    @Override
    public double getAbsoluteHeading() {
        return imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ,
                AngleUnit.DEGREES).firstAngle * multiplier;
    }

    /**
     * @return X, Y, Z angles of gyro
     */
    public double[] getAngles() {
        // make a singular hardware call
        Orientation orientation = imu.getRobotOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ,
                AngleUnit.DEGREES);

        return new double[]{orientation.firstAngle, orientation.secondAngle,
                orientation.thirdAngle};
    }

    /**
     * @return Transforms heading into {@link Rotation2d}
     */
    @Override
    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }

    @Override
    public void reset() {
        offset += getHeading();
    }

    /**
     * Inverts the ouptut of gyro
     */
    public void invertGyro() {
        multiplier *= -1;
    }

    @Override
    public void disable() {
        imu.close();
    }

    @Override
    public String getDeviceType() {
        return "REV Internal IMU";
    }

    /**
     * @return the internal sensor being wrapped
     */
    public BHI260IMU getRevImu() {
        return imu;
    }
}
