package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.DTXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

public class DriveCommand extends CommandBase{
    private DTXboxController controller;
    private DriveTrain driveTrain;

    public DriveCommand(DriveTrain driveTrain, DTXboxController controller) {
        this.controller = controller;
        this.driveTrain = driveTrain;
        addRequirements(driveTrain);
    }

    @Override
    public void execute() {
        double forwardSpeed = controller.getLeftStickYSquared();
        double rotationSpeed = -controller.getRightStickXSquared();
        if (!(controller.getRightBumper() && controller.getLeftBumper())) {
            if (Math.abs(forwardSpeed) > Constants.MAX_KIDDIE_DRIVE_VELOCITY_FORWARDS) {
                forwardSpeed = Math.copySign(Constants.MAX_KIDDIE_DRIVE_VELOCITY_FORWARDS, forwardSpeed);
            }
            if (Math.abs(rotationSpeed) > Constants.MAX_KIDDIE_DRIVE_VELOCITY_ROTATION) {
                rotationSpeed = Math.copySign(Constants.MAX_KIDDIE_DRIVE_VELOCITY_ROTATION, rotationSpeed);
            }

            
        }
        driveTrain.drive(forwardSpeed, rotationSpeed);
    }

    @Override 
    public void end(boolean isInterrupted) {
        driveTrain.drive(0, 0);
    }
}
