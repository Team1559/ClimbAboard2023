package frc.robot.subsystems;

import org.victorrobotics.dtlib.hardware.phoenix5.DTTalonSRX;
import org.victorrobotics.dtlib.subsystem.DTSubsystem;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain extends DTSubsystem {
  private final DifferentialDrive drive;
  private final DTTalonSRX        leftMotor;
  private final DTTalonSRX        rightMotor;

  public DriveTrain(int leftID, int rightID) {
    leftMotor = new DTTalonSRX(leftID);
    rightMotor = new DTTalonSRX(rightID);
    rightMotor.configOutputInverted(true);
    drive = new DifferentialDrive(leftMotor.getMotorImpl(), rightMotor.getMotorImpl());
  }

  public void drive(double forwardSpeed, double rotation) {
    drive.arcadeDrive(forwardSpeed, rotation);
  }

  @Override
  public void close() throws Exception {}
}
