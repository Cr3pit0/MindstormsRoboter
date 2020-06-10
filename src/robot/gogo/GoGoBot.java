package robot.gogo;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import robot.canbringer.StartupCompletionListener;

public class GoGoBot {


	static TouchSensor sensor1;
	static TouchSensor sensor2;
	static ColorSensor sensor3;
	
	static SuperImportantThread sit;

	static RegulatedMotor leftMotor = Motor.A;
	static RegulatedMotor rightMotor = Motor.D;
	
	static float HomeColor;
	static long startTime;
	static boolean isHome = true;
	static boolean leftHome = false;
	
	int consecutiveLeftTurns = 0;
	int consecutiveRightTurns = 0;

	public static void main(String[] args) throws InterruptedException {

		sensor1 = new TouchSensor(SensorPort.S1);
		sensor1.setDaemon(true);
		sensor1.start();

		sensor2 = new TouchSensor(SensorPort.S4);
		sensor2.setDaemon(true);
		sensor2.start();
		
		sensor3 = new ColorSensor(SensorPort.S2);
		sensor3.setDaemon(true);
		sensor3.start();
		
//		sit = new SuperImportantThread();
//		sit.setDaemon(true);
//		sit.start();
		
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		leftMotor.rotateTo(0);
		rightMotor.rotateTo(0);
		leftMotor.setSpeed(400);
		rightMotor.setSpeed(400);
		leftMotor.setAcceleration(800);
		rightMotor.setAcceleration(800);
		
		HomeColor = sensor3.getColor();

		Behavior b1 = new DriveForward();
		Behavior b2 = new DetectWall();
		Behavior b3 = new DetectHome();

		Behavior[] behaviorList = { b1, b2, b3};
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		LCD.drawString("Can Bringer", 0, 1);
		Button.LEDPattern(6);
		Button.waitForAnyPress();
		arbitrator.go();
		startTime = System.currentTimeMillis();
	}
}

class DriveForward implements Behavior {

	private boolean _suppressed = false;

	@Override
    public boolean takeControl() {
		if (Button.readButtons() != 0) {
			_suppressed = true;
			GoGoBot.leftMotor.stop();
			GoGoBot.rightMotor.stop();
			Button.LEDPattern(6);
			Button.discardEvents();
			System.out.println("Button pressed");
			if ((Button.waitForAnyPress() & Button.ID_ESCAPE) != 0) {
				Button.LEDPattern(0);
				System.exit(1);
			}
			System.out.println("Button pressed 2");
			Button.waitForAnyEvent();
			System.out.println("Button released");
		}
		return true; // this behavior always wants control.
	}

	@Override
    public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

	@Override
    public void action() {
		_suppressed = false;
		// EV3BumperCar.leftMotor.forward();
		// EV3BumperCar.rightMotor.forward();
		while (!_suppressed) {
			GoGoBot.leftMotor.backward();
			GoGoBot.rightMotor.backward();

			Thread.yield(); // don't exit till suppressed
		}
		GoGoBot.leftMotor.stop(true);
		GoGoBot.rightMotor.stop(true);
	}
}

class DetectWall implements Behavior {

	public DetectWall() {
	}

	@Override
    public boolean takeControl() {
		int touch1 = GoGoBot.sensor1.touch;
		int touch2 = GoGoBot.sensor2.touch;

		if (touch1 == 1 || touch2 == 1) {
			Button.LEDPattern(2);
			return true;
		} else {
			Button.LEDPattern(1);
			return false;
		}
	}

	@Override
    public void suppress() {
		// Since this is highest priority behavior, suppress will never be called.
	}

	@Override
    public void action() {
		
		int rotationDegree = 200;
			GoGoBot.leftMotor.rotate(rotationDegree, true);
			GoGoBot.rightMotor.rotate(rotationDegree);
			GoGoBot.leftMotor.rotate(rotationDegree, true);
			GoGoBot.rightMotor.rotate(-rotationDegree);
	}
}

class DetectHome implements Behavior {

	private boolean _suppressed = false;
	

	@Override
    public boolean takeControl() {
	    if (GoGoBot.sensor3.getColor() != GoGoBot.HomeColor) {
	        GoGoBot.leftHome = true;
	    }
	    
	    GoGoBot.isHome = GoGoBot.sensor3.getColor() == GoGoBot.HomeColor;
	    
	    return GoGoBot.leftHome && GoGoBot.isHome;
	}

	@Override
    public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

	@Override
    public void action() {
		System.exit(0);
	}
}

class TouchSensor extends Thread {

	public int touch = 255;

	EV3TouchSensor touchSensor;
	SampleProvider sp;

	TouchSensor(Port port) {
		touchSensor = new EV3TouchSensor(port);
		sp = touchSensor.getTouchMode();
	}

	@Override
    public void run() {
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			touch = (int) sample[0];
		}
	}
}

class ColorSensor extends Thread {

    private volatile float color;
    private EV3ColorSensor colorSensor;
    private SampleProvider sp;

    ColorSensor(Port port) {
        colorSensor = new EV3ColorSensor(port);
        sp = colorSensor.getColorIDMode();
    }

    float getColor() {
        return color;
    }

    @Override
    public void run() {
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            color = sample[0];
        }
    }
}
