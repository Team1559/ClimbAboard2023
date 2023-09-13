package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.DTXboxController;
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
        driveTrain.drive(controller.getLeftStickYSquared(), -controller.getRightStickXSquared());//Rotation direction needs to be inverted
    }

    @Override 
    public void end(boolean isInterrupted) {
        driveTrain.drive(0, 0);
    }
}
