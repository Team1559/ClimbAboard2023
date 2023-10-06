// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.FeederCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.FrisbeeFlinger;
import org.victorrobotics.dtlib.DTRobot;
import org.victorrobotics.dtlib.command.DTCommand;
import org.victorrobotics.dtlib.command.DTCommandScheduler;
import org.victorrobotics.dtlib.command.DTNullCommand;
import org.victorrobotics.dtlib.controller.DTXboxController;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends DTRobot {
  private final DTXboxController controller;
  private final DriveTrain       driveTrain;
  private final FrisbeeFlinger   frisbeeFlinger;

  public Robot() {
    controller = new DTXboxController(0);
    frisbeeFlinger = new FrisbeeFlinger(4, 6);
    driveTrain = new DriveTrain(5, 10);
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  protected void init() {}

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   */
  @Override
  protected void periodic() {}

  @Override
  protected void bindCommands() {
    DriveCommand driveCommand = new DriveCommand(driveTrain, controller.leftStickY.negate()
                                                                                  .squareKeepSign(),
                                                 controller.rightStickX.squareKeepSign(),
                                                 controller.leftBumper.and(controller.rightBumper));
    DTCommandScheduler.setDefaultCommand(driveTrain, driveCommand);

    controller.rightTrigger.whenGreaterOrEqual(0.5)
                           .whileTrue(new ShooterCommand(frisbeeFlinger));
    controller.leftTrigger.whenGreaterOrEqual(0.5)
                          .and(controller.rightTrigger.whenGreaterOrEqual(0.5)
                                                      .debounce(2))
                          .whileTrue(new FeederCommand(frisbeeFlinger));
  }

  @Override
  protected DTCommand getAutoCommand() {
    return new DTNullCommand();
  }

  @Override
  protected DTCommand getSelfTestCommand() {
    return new DTNullCommand();
  }

  /** This function is called once when the robot is first started up. */
  @Override
  protected void simulationInit() {}
}
