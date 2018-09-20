package robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class PilotFactory {

    private static final double WHEEL_DIAMETER = 5.6;
    private static final double WHEEL_OFFSET = 5.3; // TODO offset messen

    private static final Port LEFT_MOTOR_PORT = MotorPort.A;
    private static final Port RIGHT_MOTOR_PORT = MotorPort.D;

    public static MovePilot generate() {
        RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LEFT_MOTOR_PORT);
        RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(RIGHT_MOTOR_PORT);
        Wheel wheel1 = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(-WHEEL_OFFSET);
        Wheel wheel2 = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(WHEEL_OFFSET);
        Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
        MovePilot pilot = new MovePilot(chassis);
        pilot.setLinearSpeed(10);

        return pilot;
    }
}
