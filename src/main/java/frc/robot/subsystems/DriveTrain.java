package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class DriveTrain extends SubsystemBase{
    private DifferentialDrive driveTrain;

    public DriveTrain(int leftID, int rightID) {
        WPI_TalonSRX leftMotor = new WPI_TalonSRX(leftID);
        WPI_TalonSRX rightMotor = new WPI_TalonSRX(rightID);
        driveTrain = new DifferentialDrive(leftMotor, rightMotor);
    }

    public void drive (double leftSpeed, double rightSpeed) {
        driveTrain.tankDrive(leftSpeed, rightSpeed);
    }
}
