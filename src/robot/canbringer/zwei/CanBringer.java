package robot.canbringer.zwei;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class CanBringer {

	static final Port COLOR_SENSOR = SensorPort.S3;
	static final Port ULTRASONIC_SENSOR = SensorPort.S2;
	static final Port GYRO_SENSOR = SensorPort.S1;
	static final Port CLAW_MOTOR = MotorPort.C;

	private float HOME_COLOR = 1;

	private Arbitrator arbitrator;
	private StartupBehavior su;
	private SearchCanBehavior sc;
	private GetCanBehavior gc;
	private BringCanHomeBehavior bc;
	
	public boolean restart = true; 

	private ColorSensor color;
	private GyroSensor gyro;
	private UltrasonicSensor ultrasonic;

	private MovePilot pilot;
	private Claw claw;

	public static void main(String[] args) {
		new CanBringer();
	}

	public CanBringer() {
		System.out.println("Initializing...");

		StartupCompletionListener startupCompletionListener = new StartupCompletionListener() {

			@Override
			protected void onComplete() {
				System.out.println("Initialization finished - Press any button to start");
				Button.LEDPattern(2);
				Button.waitForAnyPress();
				System.out.println("start");
				arbitrator.go();

			}
		};

		Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 56).offset(-94);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 56).offset(94);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, 2);
		pilot = new MovePilot(chassis);
		pilot.setAngularSpeed(30);

		claw = new Claw(CLAW_MOTOR);
		ultrasonic = new UltrasonicSensor(startupCompletionListener, ULTRASONIC_SENSOR);
		gyro = new GyroSensor(startupCompletionListener, GYRO_SENSOR);
		color = new ColorSensor(startupCompletionListener, COLOR_SENSOR);

		su = new StartupBehavior(this);
		sc = new SearchCanBehavior(this);
		gc = new GetCanBehavior(this);
		bc = new BringCanHomeBehavior(this);
		arbitrator = new Arbitrator(new Behavior[] { sc, bc, gc, su });

		new Thread(ultrasonic).start();
		new Thread(gyro).start();
		new Thread(color).start();
	}

	public ColorSensor getColor() {
		return color;
	}

	public GyroSensor getGyro() {
		return gyro;
	}

	public UltrasonicSensor getUltrasonic() {
		return ultrasonic;
	}

	public MovePilot getPilot() {
		return pilot;
	}

	public Claw getClaw() {
		return claw;
	}

	public float getHomeColor() {
		return HOME_COLOR;
	}

	public void setHomeColor(float hc) {
		HOME_COLOR = hc;
	}
}

class SearchCanBehavior implements Behavior {

	private int ROTATION_STEPS = 5;
	private double DOSE_GEFUNDEN = 0.5;

	private boolean suppressed = false;

	private CanBringer cb;

	public SearchCanBehavior(CanBringer cb) {
		this.cb = cb;
	}

	@Override
	public boolean takeControl() {
		if(cb.restart) {
			cb.restart = false;
			return true;
		}
	
		return false;
	}

	@Override
	public void action() {
		suppressed = false;

		while (!suppressed) {
			cb.getClaw().open();
			System.out.println("Begin Search...");

			int vorzeichen = -1;
			for (int i = 0; i < 100; i++) {

				cb.getPilot().rotate(vorzeichen * ROTATION_STEPS * i, true);

				if (cb.getUltrasonic().getDistance() <= DOSE_GEFUNDEN) {
					break;
				}

				vorzeichen *= vorzeichen;
			}

			cb.getPilot().forward();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}

class GetCanBehavior implements Behavior {
	private double INKLAUE = 0.13;

	private boolean suppressed = false;

	private CanBringer cb;

	public GetCanBehavior(CanBringer cb) {
		this.cb = cb;
	}

	@Override
	public boolean takeControl() {

		if (cb.getUltrasonic().getDistance() <= INKLAUE) {
			return true;
		}

		return false;
	}

	@Override
	public void action() {
		suppressed = false;

		while (!suppressed) {
			cb.getPilot().stop();
			cb.getClaw().close();
			cb.getPilot().rotate(180);
			cb.getPilot().forward();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}

class BringCanHomeBehavior implements Behavior {

	private boolean suppressed = false;
	private double COLOR_BEREICH = 0.1; 
	private CanBringer cb;

	public BringCanHomeBehavior(CanBringer cb) {
		this.cb = cb;
	}

	@Override
	public boolean takeControl() {
		if (cb.getUltrasonic().getDistance() == Double.POSITIVE_INFINITY 
				&& (cb.getColor().getColor() > cb.getHomeColor() + COLOR_BEREICH 
				|| cb.getColor().getColor() < cb.getHomeColor() - COLOR_BEREICH)) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		suppressed = false;

		while (!suppressed) {
			// do something
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}

class ReleaseCanBehavior implements Behavior {
	
	private double COLOR_BEREICH = 0.1; 

	private boolean suppressed = false;

	private CanBringer cb;

	public ReleaseCanBehavior(CanBringer cb) {
		this.cb = cb;
	}

	@Override
	public boolean takeControl() {
		if (cb.getUltrasonic().getDistance() == Double.POSITIVE_INFINITY
				&& (cb.getColor().getColor() <= cb.getHomeColor() + COLOR_BEREICH 
					&& cb.getColor().getColor() >= cb.getHomeColor() - COLOR_BEREICH)) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		suppressed = false;

		while (!suppressed) {
			cb.getClaw().open();
			cb.getPilot().travel(-20);
			cb.getPilot().rotate(180);
			cb.restart = true;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}

class StartupBehavior implements Behavior {

	private boolean initialized = false;

	private CanBringer cb;

	public StartupBehavior(CanBringer cb) {
		this.cb = cb;
	}

	@Override
	public boolean takeControl() {
		return !initialized;
	}

	@Override
	public void action() {
		cb.getClaw().close();
		cb.setHomeColor(cb.getColor().getColor());
		initialized = true;
	}

	@Override
	public void suppress() {
	}
}
