package frc.robot.commands;

import frc.robot.subsystems.FrisbeeFlinger;
import org.victorrobotics.dtlib.command.DTCommandBase;

public class ShooterCommand extends DTCommandBase {
  private FrisbeeFlinger flinger;

  public ShooterCommand(FrisbeeFlinger flinger) {
    this.flinger = flinger;
  }

  @Override
  public void execute() {
    flinger.spinShooter(1);
  }

  @Override
  public void end() {
    flinger.stopShooter();
  }
}
