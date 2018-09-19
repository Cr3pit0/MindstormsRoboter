package robot.gogo;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class GoGoBot {

	public static void introMessage() {

		GraphicsLCD g = LocalEV3.get().getGraphicsLCD();
		g.drawString("Bumper Car Demo", 5, 0, 0);
		g.setFont(Font.getSmallFont());
		g.drawString("Demonstration of the Behavior", 2, 20, 0);
		g.drawString("subsumption classes. Requires", 2, 30, 0);
		g.drawString("a wheeled vehicle with two", 2, 40, 0);
		g.drawString("independently controlled", 2, 50, 0);
		g.drawString("motors connected to motor", 2, 60, 0);
		g.drawString("ports B and C, and an", 2, 70, 0);
		g.drawString("infrared sensor connected", 2, 80, 0);
		g.drawString("to port 4.", 2, 90, 0);

		// Quit GUI button:
		g.setFont(Font.getSmallFont()); // can also get specific size using Font.getFont()
		int y_quit = 100;
		int width_quit = 45;
		int height_quit = width_quit / 2;
		int arc_diam = 6;
		g.drawString("QUIT", 9, y_quit + 7, 0);
		g.drawLine(0, y_quit, 45, y_quit); // top line
		g.drawLine(0, y_quit, 0, y_quit + height_quit - arc_diam / 2); // left line
		g.drawLine(width_quit, y_quit, width_quit, y_quit + height_quit / 2); // right line
		g.drawLine(0 + arc_diam / 2, y_quit + height_quit, width_quit - 10, y_quit + height_quit); // bottom line
		g.drawLine(width_quit - 10, y_quit + height_quit, width_quit, y_quit + height_quit / 2); // diagonal
		g.drawArc(0, y_quit + height_quit - arc_diam, arc_diam, arc_diam, 180, 90);

		// Enter GUI button:
		g.fillRect(width_quit + 10, y_quit, height_quit, height_quit);
		g.drawString("GO", width_quit + 15, y_quit + 7, 0, true);

		Button.waitForAnyPress();
		if (Button.ESCAPE.isDown())
			System.exit(0);
		g.clear();
	}

	static TouchSensor sensor1;
	static TouchSensor sensor2;

	static RegulatedMotor leftMotor = Motor.B;
	static RegulatedMotor rightMotor = Motor.C;

	public static void main(String[] args) throws InterruptedException {

		introMessage();

		sensor1 = new TouchSensor(SensorPort.S1);
		sensor1.setDaemon(true);
		sensor1.start();

		sensor2 = new TouchSensor(SensorPort.S4);
		sensor2.setDaemon(true);
		sensor2.start();

		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		leftMotor.rotateTo(0);
		rightMotor.rotateTo(0);
		leftMotor.setSpeed(400);
		rightMotor.setSpeed(400);
		leftMotor.setAcceleration(800);
		rightMotor.setAcceleration(800);

		Behavior b1 = new DriveForward();
		Behavior b2 = new DetectWall();

		Behavior[] behaviorList = { b1, b2 };
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		LCD.drawString("Bumper Car", 0, 1);
		Button.LEDPattern(6);
		Button.waitForAnyPress();
		arbitrator.go();
	}
}

class DriveForward implements Behavior {

	private boolean _suppressed = false;

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

	public void suppress() {
		_suppressed = true;// standard practice for suppress methods
	}

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
		// touch = new TouchSensor(SensorPort.S1);
		// sonar = new UltrasonicSensor(SensorPort.S3);
	}

	private boolean checkTouched() {

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

	public boolean takeControl() {
		return checkTouched();
	}

	public void suppress() {
		// Since this is highest priority behavior, suppress will never be called.
	}

	public void action() {
		
		int drive = 0;
		
		if (GoGoBot.sensor1.touch == 1 && GoGoBot.sensor2.touch == 0) {
			drive = 1;
		} else if (GoGoBot.sensor1.touch == 0 && GoGoBot.sensor2.touch == 1) {
			drive = 2;
		}

		switch (drive) {
		case 0:
			GoGoBot.leftMotor.rotate(180, true);
			GoGoBot.rightMotor.rotate(180);
			break;
		case 1:
			GoGoBot.leftMotor.rotate(180, true);
			GoGoBot.rightMotor.rotate(180);
//			GoGoBot.rightMotor.rotate(360, true);
			GoGoBot.leftMotor.rotate(180);

			break;
		case 2:
			GoGoBot.leftMotor.rotate(180, true);
			GoGoBot.rightMotor.rotate(180);
//			GoGoBot.leftMotor.rotate(360, true);
			GoGoBot.rightMotor.rotate(180);
			break;
		default:

			break;
		}
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

	public void run() {
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			touch = (int) sample[0];
		}
	}
}
