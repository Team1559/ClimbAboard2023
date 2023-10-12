package frc.robot.subsystems;

import frc.robot.Constants;

import org.victorrobotics.dtlib.hardware.phoenix5.DTTalonSRX;
import org.victorrobotics.dtlib.subsystem.DTSubsystem;

public class FrisbeeFlinger extends DTSubsystem {
  private DTTalonSRX feederMotor;

  private DTTalonSRX shooterMotor;

  public FrisbeeFlinger(int feederID, int shooterID) {
    feederMotor = new DTTalonSRX(feederID);
    shooterMotor = new DTTalonSRX(shooterID);
  }

  public void spinShooter(double speed) {
    shooterMotor.setPercentOutput(speed);
  }

  public void stopShooter() {
    shooterMotor.neutralOutput();
  }

  public void spinFeeder() {
    feederMotor.setPercentOutput(Constants.FEEDER_SPEED);
  }

  public void stopFeeder() {
    feederMotor.neutralOutput();
  }

  @Override
  public void close() throws Exception {}
}
