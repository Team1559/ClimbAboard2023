package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FrisbeeFlinger extends SubsystemBase {
    private WPI_TalonSRX feederMotor;
    private WPI_TalonSRX shooterMotor;

    public FrisbeeFlinger(int feederID, int shooterID){
        feederMotor = new WPI_TalonSRX(feederID);
        shooterMotor = new WPI_TalonSRX(shooterID);
    }

    public void spinShooter(double speed){
        shooterMotor.set(TalonSRXControlMode.PercentOutput, speed);
    }
    
    public void stopShooter(){
        shooterMotor.set(TalonSRXControlMode.PercentOutput, 0);
    }
    
    public void spinFeeder(){
        feederMotor.set(TalonSRXControlMode.PercentOutput,Constants.FEEDER_SPEED); 
    }

    public void stopFeeder(){
        feederMotor.set(TalonSRXControlMode.PercentOutput,0);
    }
}
