package robot.canbringer.zwei;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import robot.Level;
import robot.Logger;

public class CanBringer {

    static final Port COLOR_SENSOR = SensorPort.S2;
    static final Port ULTRASONIC_SENSOR = SensorPort.S3;
    static final Port TOUCH_SENSOR_LEFT = SensorPort.S1;
    static final Port TOUCH_SENSOR_RIGHT = SensorPort.S4;
	static final Port CLAW_MOTOR = MotorPort.C;
    static final double OBSTACLE_DISTANCE = 0.2;

    private float HomeColor = 1;
    private boolean isOutOfBase = false;
    private boolean isFinished = false;
    public int rounds = 1;

	private Arbitrator arbitrator;
	private StartupBehavior su;
    private JustDriveForwardBehavior jdf;
    private ObstacleRightBehavior or;
    private ObstacleLeftBehavior ol;

	private ColorSensor color;
	private UltrasonicSensor ultrasonic;
    private TouchSensor touchLeft;
    private TouchSensor touchRight;

	private MovePilot pilot;

	public static void main(String[] args) {
		new CanBringer();
	}

	public CanBringer() {
        Logger.log(Level.INFO, "Initializing...");
		StartupCompletionListener startupCompletionListener = new StartupCompletionListener() {

			@Override
			protected void onComplete() {
                Logger.log(Level.INFO, "Initialization finished - Press any button to start");
				Button.LEDPattern(2);
				Button.waitForAnyPress();
                Logger.log(Level.INFO, "Starting...");
				arbitrator.go();

			}
		};

        Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 56).offset(-67).gearRatio(3).invert(true);
        Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 56).offset(67).gearRatio(3).invert(true);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		pilot = new MovePilot(chassis);
        pilot.setAngularSpeed(50);
        pilot.setLinearSpeed(100);

		ultrasonic = new UltrasonicSensor(startupCompletionListener, ULTRASONIC_SENSOR);
		color = new ColorSensor(startupCompletionListener, COLOR_SENSOR);
        touchLeft = new TouchSensor(startupCompletionListener, TOUCH_SENSOR_LEFT);
        touchRight = new TouchSensor(startupCompletionListener, TOUCH_SENSOR_RIGHT);

		su = new StartupBehavior(this);
        jdf = new JustDriveForwardBehavior(this);
        or = new ObstacleRightBehavior(this);
        ol = new ObstacleLeftBehavior(this);
        arbitrator = new Arbitrator(new Behavior[] { jdf, or, ol, su });

        ultrasonic.start();
		color.start();
        touchLeft.start();
        touchRight.start();
	}

	public ColorSensor getColor() {
		return color;
	}

	public UltrasonicSensor getUltrasonic() {
		return ultrasonic;
	}

    public TouchSensor getTouchLeft() {
        return touchLeft;
		}

    public TouchSensor getTouchRight() {
        return touchRight;
	}

    public MovePilot getPilot() {
        return pilot;
	}

    public float getHomeColor() {
        return HomeColor;
	}

    public void setHomeColor(float hc) {
        HomeColor = hc;
}

    public boolean isOutOfBase() {
        return this.isOutOfBase;
	}

    public void setIsOutOfBase(boolean b) {
        isOutOfBase = b;
	}

    public boolean isFinished() {
        return this.isFinished;
	}

    public void setIsFinished(boolean b) {
        isFinished = b;
	}
}
