package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.victorrobotics.dtlib.command.DTCommandBase;

public class DriveCommand extends DTCommandBase {
  private final DoubleSupplier  forwardSupplier;
  private final DoubleSupplier  rotationSupplier;
  private final BooleanSupplier speedUnlocker;
  private final DriveTrain      driveTrain;

  public DriveCommand(DriveTrain driveTrain, DoubleSupplier forward, DoubleSupplier rotation,
                      BooleanSupplier speedUp) {
    forwardSupplier = forward;
    rotationSupplier = rotation;
    speedUnlocker = speedUp;
    this.driveTrain = driveTrain;
    addRequirements(driveTrain);
  }

  @Override
  public void execute() {
    double forwardSpeed = forwardSupplier.getAsDouble();
    double rotationSpeed = rotationSupplier.getAsDouble();
    if (!speedUnlocker.getAsBoolean()) {
      if (Math.abs(forwardSpeed) > Constants.MAX_KIDDIE_DRIVE_VELOCITY_FORWARDS) {
        forwardSpeed = Math.copySign(Constants.MAX_KIDDIE_DRIVE_VELOCITY_FORWARDS, forwardSpeed);
      }
      if (Math.abs(rotationSpeed) > Constants.MAX_KIDDIE_DRIVE_VELOCITY_ROTATION) {
        rotationSpeed = Math.copySign(Constants.MAX_KIDDIE_DRIVE_VELOCITY_ROTATION, rotationSpeed);
      }
    }
    driveTrain.drive(forwardSpeed, rotationSpeed);
    System.out.println(forwardSpeed);
  }

  @Override
  public void end() {
    driveTrain.drive(0, 0);
  }
}
