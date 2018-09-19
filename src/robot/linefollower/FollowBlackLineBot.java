package robot.linefollower;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class FollowBlackLineBot {

	static BlackLineSensor sensor1;

	static RegulatedMotor leftMotor = Motor.B;
	static RegulatedMotor rightMotor = Motor.C;


	public static void main(String[] args) throws InterruptedException {

		sensor1 = new BlackLineSensor(SensorPort.S1);
		sensor1.setDaemon(true);
		sensor1.start();

//		leftMotor.resetTachoCount();
//		rightMotor.resetTachoCount();
//		leftMotor.rotateTo(0);
//		rightMotor.rotateTo(0);
//		leftMotor.setSpeed(400);
//		rightMotor.setSpeed(400);
//		leftMotor.setAcceleration(800);
//		rightMotor.setAcceleration(800);

		while (true) {
			while(sensor1.blackLineFound) {
				System.out.println("Gefunden, " +  sensor1.intensity);
//				drive(0, 0);
			}

			while(sensor1.blackLineFound == false) {
				System.out.println("Und weg..., " +  sensor1.intensity);
//				drive(3, 0);
//				drive(1, 90);
//				drive(2, 90);
//				drive(2, 90);
//				drive(1, 90);
			}
		}
	}

	public static void drive(int Richtung, int rotateProzent) {

		// Vorne = 0
		// Zurück = 3
		// Links = 1
		// Rechts = 2

		if (rotateProzent > 0) {
			switch (Richtung) {
			case 0:
				FollowBlackLineBot.leftMotor.forward();
				FollowBlackLineBot.rightMotor.forward();
			case 1:
				FollowBlackLineBot.leftMotor.rotate(rotateProzent, true);
				FollowBlackLineBot.rightMotor.rotate(-rotateProzent);
			case 2:
				FollowBlackLineBot.leftMotor.rotate(-rotateProzent, true);
				FollowBlackLineBot.rightMotor.rotate(rotateProzent);
			case 3:
				FollowBlackLineBot.leftMotor.backward();
				FollowBlackLineBot.rightMotor.backward();
			default:
				FollowBlackLineBot.leftMotor.stop();
				FollowBlackLineBot.rightMotor.stop();
				break;
			}
		} else {
			switch (Richtung) {
			case 0:
				FollowBlackLineBot.leftMotor.forward();
				FollowBlackLineBot.rightMotor.forward();
			case 1:
				FollowBlackLineBot.leftMotor.forward();
				FollowBlackLineBot.rightMotor.backward();
			case 2:
				FollowBlackLineBot.leftMotor.backward();
				FollowBlackLineBot.rightMotor.forward();
			case 3:
				FollowBlackLineBot.leftMotor.backward();
				FollowBlackLineBot.rightMotor.backward();
			default:
				FollowBlackLineBot.leftMotor.stop();
				FollowBlackLineBot.rightMotor.stop();
				break;
			}
		}
	}
}

class BlackLineSensor extends Thread {

	public float intensity = 255;
	
	public float minimum; 
	public static float minimumRange = 0.2f;
	public float maximum;
	public static float maximumRange = 0.2f;
	public boolean blackLineFound = false; 
	
	EV3ColorSensor colorSensor;
	SampleProvider sp;

	BlackLineSensor(Port port) {
		colorSensor = new EV3ColorSensor(port);
		sp = colorSensor.getRedMode();
	}

	public void run() {
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			intensity = (float) sample[0];
			blackLineFound = intensity < minimum + minimumRange;
		}
	}
}
