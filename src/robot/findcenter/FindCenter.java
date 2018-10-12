package robot.findcenter;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import robot.Pilot;

public class FindCenter {

	static final Port ULTRASONIC_SENSOR = SensorPort.S2;
	static final Port GYRO_SENSOR = SensorPort.S3;

	static SampleProvider ultrasonic;
	static SampleProvider gyro;

	public static void main(String[] args) throws InterruptedException {
		Pilot pilot = new Pilot(80);

		EV3UltrasonicSensor pro = new EV3UltrasonicSensor(ULTRASONIC_SENSOR);
		ultrasonic = pro.getDistanceMode();
		
		EV3GyroSensor gyroSensor = new EV3GyroSensor(GYRO_SENSOR);
		gyro = gyroSensor.getAngleMode();
		gyroSensor.reset();
		
		while (true) {
			System.out.println(getCurrentAngle());
		}

//		float[] distance = new float[4];
//		for (int i = 0; i < 4; i++) {
//			distance[i] = getCurrentDistance() * 100;
//			Thread.sleep(2000);
//			System.out.println(distance[i]);
//			while (getCurrentAngle() < 90) {
//				pilot.spinRight();
//			}
//			pilot.stop();
//			gyroSensor.reset();
////			pilot.rotateBy(90);
//		}
//		// Roboter steht wieder in Ausgangsposition
//
//		// TODO Roboter muss exakt auf der Stelle drehen!
//		System.out.println((distance[0] - distance[2]) / 2);
//		System.out.println((int) (distance[0] - distance[2]) / 2);
//		System.out.println((distance[1] - distance[3]) / 2);
//		System.out.println((int) (distance[1] - distance[3]) / 2);
////		Thread.sleep(3000);
//
//		pilot.drive((int) ((distance[0] - distance[2]) / 2));
//		while (getCurrentAngle() < 90) {
//			pilot.spinRight();
//		}
////		pilot.rotateBy(90);
//		pilot.stop();
//		pilot.drive((int) ((distance[1] - distance[3]) / 2));
//		pro.close();
//		gyroSensor.close();
	}

	private static float getCurrentDistance() {
		float[] sample = new float[ultrasonic.sampleSize()];
		ultrasonic.fetchSample(sample, 0);
		return sample[0];
	}
	
	private static float getCurrentAngle() {
		float[] sample = new float[gyro.sampleSize()];
		gyro.fetchSample(sample, 0);
		System.out.println("Angle: " + sample[0]);
		return Math.abs(sample[0]);
	}
}