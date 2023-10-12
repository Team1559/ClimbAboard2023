package frc.robot.commands;

import frc.robot.subsystems.FrisbeeFlinger;

import org.victorrobotics.dtlib.command.DTCommandBase;

public class FeederCommand extends DTCommandBase {
  private FrisbeeFlinger flinger;

  public FeederCommand(FrisbeeFlinger flinger) {
    this.flinger = flinger;
  }

  @Override
  public void execute() {
    flinger.spinFeeder();
  }

  @Override
  public void end() {
    flinger.stopFeeder();
  }
}
