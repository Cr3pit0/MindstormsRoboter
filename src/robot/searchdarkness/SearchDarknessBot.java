
package robot.searchdarkness;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class SearchDarknessBot {

	static SearchDarknessSensor sensor1;

	static RegulatedMotor leftMotor = Motor.B;
	static RegulatedMotor rightMotor = Motor.C;


	public static void main(String[] args) throws InterruptedException {

		sensor1 = new SearchDarknessSensor(SensorPort.S1);
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
			
			boolean goLeft = true; 
			
			while(sensor1.isGoingDarker) {
				System.out.println("Dunkler, " +  sensor1.intensity);
				
//				drive(0, 20);
//				drive(1, 20);
			}

			while(sensor1.isGoingDarker == false) {
				System.out.println("Heller, " +  sensor1.intensity);
				
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
				FollowBlackLineBot.leftMotor.rotate(rotateProzent);
				FollowBlackLineBot.rightMotor.rotate(rotateProzent);
			case 1:
				FollowBlackLineBot.leftMotor.rotate(rotateProzent, true);
				FollowBlackLineBot.rightMotor.rotate(-rotateProzent);
			case 2:
				FollowBlackLineBot.leftMotor.rotate(-rotateProzent, true);
				FollowBlackLineBot.rightMotor.rotate(rotateProzent);
			case 3:
				FollowBlackLineBot.leftMotor.rotate(-rotateProzent);
				FollowBlackLineBot.rightMotor.rotate(-rotateProzent);
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

class SearchDarknessSensor extends Thread {

	public float intensity = 255;
	public float minimum;
	public boolean isGoingDarker = false; 
	
	EV3ColorSensor colorSensor;
	SampleProvider sp;

	SearchDarknessSensor(Port port) {
		colorSensor = new EV3ColorSensor(port);
		sp = colorSensor.getRedMode();
		minimum = Float.POSITIVE_INFINITY;
	}

	public void run() {
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			intensity = (float) sample[0];
			isGoingDarker = intensity < minimum;
		}
	}
}
