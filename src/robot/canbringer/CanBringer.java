package robot.canbringer;

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

public class CanBringer{

    static final Port COLOR_SENSOR = SensorPort.S3;
    static final Port ULTRASONIC_SENSOR = SensorPort.S2;
    static final Port GYRO_SENSOR = SensorPort.S1;

    static final Port CLAW_MOTOR = MotorPort.C;

    private Arbitrator arbitrator;
    private StartupBehavior su;
    private SearchCanBehavior sc;
    private BringCanHomeBehavior bc;

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
        OdometryPoseProvider posePro = new OdometryPoseProvider(pilot);
        Navigator navi = new Navigator(pilot);
        navi.setPoseProvider(posePro);
        // Navigate via:
        // navi.addWaypoint(x, y, heading);
        claw = new Claw(CLAW_MOTOR);

        ultrasonic = new UltrasonicSensor(startupCompletionListener, ULTRASONIC_SENSOR);
        gyro = new GyroSensor(startupCompletionListener, GYRO_SENSOR);
        color = new ColorSensor(startupCompletionListener, COLOR_SENSOR);

        su = new StartupBehavior(this);
        sc = new SearchCanBehavior(this);
        bc = new BringCanHomeBehavior(this);
        arbitrator = new Arbitrator(new Behavior[] { sc, bc, su });

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
}

class SearchCanBehavior implements Behavior {

    private boolean suppressed = false;

    private CanBringer cb;

    public SearchCanBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void action() {
        suppressed = false;

        while (!suppressed) {
            // cb.getClaw().adjust(-100);
            cb.getClaw().open();
            // System.out.println("rotate");
            // cb.getPilot().rotate(360);
            // cb.getPilot().travel(80);
            // for (int i = 0; i < 6; i++) {
            // cb.getPilot().rotate(10 * i);
            // }
            // cb.getClaw().open();
            // cb.getClaw().close();
            if (!cb.getPilot().isMoving()) {
                cb.getPilot().rotate(720, true);
            }
            cb.getClaw().close();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.exit(0);
        }
    }

    @Override
    public void suppress() {
        suppressed = true;
    }
}

class BringCanHomeBehavior implements Behavior {

    private boolean suppressed = false;

    private CanBringer cb;

    public BringCanHomeBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        return false; // TODO hasCan()?
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
        initialized = true;
    }

    @Override
    public void suppress() {
    }
}
