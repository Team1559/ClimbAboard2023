package org.victorrobotics.dtlib.command;

import org.victorrobotics.dtlib.DTRobot;
import org.victorrobotics.dtlib.subsystem.DTSubsystem;

import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * A state machine representing a complete action to be performed. Commands are
 * run by the {@link DTCommandScheduler}, and can be composed into groups to
 * allow users to build complicated multistep actions without the need to write
 * complicated state machine logic themselves. By default, commands are run
 * sequentially from the main robot loop.
 */
public interface DTCommand {
  /**
   * Specifies the subsystems used by the command. Two commands cannot use the
   * same subsystem at the same time. If another command is scheduled that
   * shares a requirement, {@link #isInterruptible()} will be checked,
   * unscheduling one of them.
   *
   * @return the set of required subsystems (may be empty)
   */
  default Set<DTSubsystem> getRequirements() {
    return Set.of();
  }

  /**
   * The initialization of a command. Called once each time a command is
   * started.
   */
  default void initialize() {}

  /**
   * The main body of a command. Called every loop iteration while scheduled.
   */
  default void execute() {}

  /**
   * Whether the command has finished. Once a command finishes, the scheduler
   * will call its {@link #end()} method and un-schedule it.
   *
   * @return whether the command has finished
   */
  default boolean isFinished() {
    return true;
  }

  /**
   * The action to take when the command ends. Called once {@link #isFinished()}
   * returns true.
   */
  default void end() {}

  /**
   * The action to take when the scheduler interrupts the command. By default,
   * this simply redirects to {@link #end()}.
   */
  default void interrupt() {
    end();
  }

  /**
   * Whether the command completed successfully. Used to determine pass/fail of
   * a robot self test.
   *
   * @return whether the command completed without issue
   *
   * @see DTRobot#getSelfTestCommand()
   */
  default boolean wasSuccessful() {
    return true;
  }

  /**
   * Whether the command is permitted to be interrupted by the scheduler. If
   * true, scheduling another command that requires one or more of the command's
   * requirements will result in interruption. Regardless of interruption
   * behavior, commands may still be canceled explicitly.
   * <p>
   * By default, commands are interruptible unless overriden.
   *
   * @return whether the command can be interrupted
   *
   * @see #interrupt()
   * @see DTCommandScheduler#cancel(DTCommand)
   */
  default boolean isInterruptible() {
    return true;
  }

  /**
   * Whether the command is permitted to remain scheduled when the robot is
   * disabled. Certain command types, for example {@link DTPrintCommand}, should
   * not be hindered based on enable status.
   *
   * @return whether the command runs when the robot is disabled
   *
   * @see DTRobot#getCurrentMode()
   */
  default boolean runsWhenDisabled() {
    return false;
  }

  /**
   * The name of the command. Useful for epoch prints. Defaults to the command
   * class name.
   *
   * @return the name of the command
   */
  default String getName() {
    return getClass().getSimpleName();
  }

  /**
   * @param requirement
   *        the subsystem to check
   *
   * @return whether the command requires the subsystem
   */
  default boolean hasRequirement(DTSubsystem requirement) {
    return getRequirements().contains(requirement);
  }

  /**
   * Schedules the command for execution. This is a convenience method
   * equivalent to
   *
   * <pre>
   * DTCommandScheduler.schedule(this)
   * </pre>
   *
   * @see DTCommandScheduler#schedule(DTCommand)
   */
  default void schedule() {
    DTCommandScheduler.schedule(this);
  }

  /**
   * Cancels the command if currently scheduled. This is a convenience method
   * equivalent to
   *
   * <pre>
   * DTCommandScheduler.cancel(this)
   * </pre>
   *
   * @see DTCommandScheduler#cancel(DTCommand)
   */
  default void cancel() {
    DTCommandScheduler.cancel(this);
  }

  /**
   * Whether the command is currently scheduled for execution. This is a
   * convenience method equivalent to
   *
   * <pre>
   * DTCommandScheduler.isScheduled(this)
   * </pre>
   *
   * @return whether the command is scheduled
   */
  default boolean isScheduled() {
    return DTCommandScheduler.isScheduled(this);
  }

  /**
   * Decorates this command with an interrupt timeout. If the timeout is past
   * before this command finishes, it will be interrupted.
   *
   * @param seconds
   *        the timeout duration
   *
   * @return the decorated command
   *
   * @see DTRaceCommandGroup
   */
  default DTRaceCommandGroup withTimeout(double seconds) {
    return raceWith(new DTWaitCommand(seconds));
  }

  /**
   * Decorates this command with an interrupt condition. If the condition is met
   * before this command finishes, it will be interrupted.
   *
   * @param condition
   *        the interrupt condition
   *
   * @return the decorated command
   *
   * @see DTRaceCommandGroup
   */
  default DTRaceCommandGroup until(BooleanSupplier condition) {
    return raceWith(new DTWaitUntilCommand(condition));
  }

  /**
   * If the condition is true when this command is scheduled, ignores this
   * command's exeecution.
   *
   * @param condition
   *        the condition
   *
   * @return the decorated command
   *
   * @see DTConditionalCommand
   */
  default DTConditionalCommand unless(BooleanSupplier condition) {
    return new DTConditionalCommand(new DTNullCommand(), this, condition);
  }

  /**
   * If the condition is false when this command is scheduled, ignores this
   * command's execution.
   *
   * @param condition
   *        the condition
   *
   * @return the decorated command
   *
   * @see DTConditionalCommand
   */
  default DTConditionalCommand onlyIf(BooleanSupplier condition) {
    return new DTConditionalCommand(this, new DTNullCommand(), condition);
  }

  /**
   * Decorates this command with commands to run before this command starts.
   *
   * @param before
   *        the command(s) to run before this one
   *
   * @return the decorated command
   *
   * @see DTSequentialCommandGroup
   */
  default DTSequentialCommandGroup beforeStarting(DTCommand... before) {
    return new DTSequentialCommandGroup(before).andThen(this);
  }

  /**
   * Decorates this command with commands to be run afterwards in sequence.
   *
   * @param next
   *        the command(s) to run next
   *
   * @return the decorated command
   *
   * @see DTSequentialCommandGroup
   */
  default DTSequentialCommandGroup andThen(DTCommand... next) {
    return new DTSequentialCommandGroup(this).andThen(next);
  }

  /**
   * Decorates this command with commands to run parallel to it, ending all
   * commands have ended.
   *
   * @param parallel
   *        the command(s) to run in parallel
   *
   * @return the decorated command
   *
   * @see DTParallelCommandGroup
   */
  default DTParallelCommandGroup alongWith(DTCommand... parallel) {
    return new DTParallelCommandGroup(this).alongWith(parallel);
  }

  /**
   * Decorates this command with commands to run parallel to it, ending when the
   * first command ends and interrupting the rest.
   *
   * @param parallel
   *        the command(s) to run in parallel
   *
   * @return the decorated command
   *
   * @see DTRaceCommandGroup
   */
  default DTRaceCommandGroup raceWith(DTCommand... parallel) {
    return new DTRaceCommandGroup(this).raceWith(parallel);
  }

  /**
   * Decorates this command to run repeatedly, restarting once it finishes,
   * forever until it is interrupted. It can still be canceled.
   *
   * @return the decorated command
   *
   * @see DTRepeatCommand
   */
  default DTRepeatCommand repeatedly() {
    return new DTRepeatCommand(this);
  }

  /**
   * Decorates this command to run repeatedly, restarting once it finishes,
   * until the specified condition is met or it is interrupted. It can still be
   * canceled.
   *
   * @param condition
   *        the end condition
   *
   * @return the decorated command
   *
   * @see DTRepeatCommand
   * @see DTRaceCommandGroup
   */
  default DTRaceCommandGroup repeatUntil(BooleanSupplier condition) {
    return new DTRepeatCommand(this).until(condition);
  }

  /**
   * Decorates this command to catch all exceptions, rather than propogating
   * them to the scheduler.
   *
   * @return the decorated command
   *
   * @see DTRecoveryCommand
   */
  default DTRecoveryCommand catchExceptions() {
    return new DTRecoveryCommand(this);
  }

  /**
   * Decorates this command as a proxy target, effectively separating the
   * command's attributes (e.g. name, requirements) from its execution.
   *
   * @return the decorated command
   *
   * @see DTProxyCommand
   */
  default DTProxyCommand proxy() {
    return new DTProxyCommand(this);
  }

  /**
   * Decorates this command to have the given disabled behavior.
   *
   * @param runsWhenDisabled
   *        whether to run when the robot is disabled
   *
   * @return the decorated command
   *
   * @see #runsWhenDisabled()
   */
  default DTTargetCommand overrideDisable(boolean runsWhenDisabled) {
    return new DTTargetCommand(this) {
      @Override
      public boolean runsWhenDisabled() {
        return runsWhenDisabled;
      }
    };
  }

  /**
   * Decorates this command to have the given inteerruption behavior.
   *
   * @param isInterruptible
   *        whether the command should be interruptible
   *
   * @return the decorated command
   *
   * @see #isInterruptible()
   */
  default DTTargetCommand overrideInterrupt(boolean isInterruptible) {
    return new DTTargetCommand(this) {
      @Override
      public boolean isInterruptible() {
        return isInterruptible;
      }
    };
  }

  /**
   * Decorates this command with a new name, which is useful for logging and
   * debugging.
   *
   * @param name
   *        the new name
   *
   * @return the decorated command
   *
   * @see #getName()
   */
  default DTTargetCommand withName(String name) {
    return new DTTargetCommand(this) {
      @Override
      public String getName() {
        return name;
      }
    };
  }
}