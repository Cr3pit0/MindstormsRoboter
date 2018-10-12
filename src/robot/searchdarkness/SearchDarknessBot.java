package robot.searchdarkness;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import robot.Pilot;

public class SearchDarknessBot {

	static final Port LIGHT_SENSOR_PORT = SensorPort.S1;
	
	static final int ROTATE_ANGLE = 5;
	static final int TRAVEL_DISTANCE = 15;

	public static void main(String[] args) {
		Pilot pilot = new Pilot(40);
		int direction = -1;
		boolean finished = false;

		Sensor sensor = new Sensor();
		Thread t = new Thread(sensor);
		t.start();

		while (!finished) {
			if (!sensor.ready) {
				continue;
			}
			if (sensor.darker) {
				pilot.forward();
			} else {
				float current = sensor.currentLevel;
				pilot.rotateBy(direction * ROTATE_ANGLE); // drehe 5 Grad in bevorzugte Richtung
				pilot.drive(TRAVEL_DISTANCE);
				if (sensor.currentLevel > current) { // falls heller nach Drehung
					direction *= -1; // aendere bevorzugte Richtung
					pilot.drive(-TRAVEL_DISTANCE);
					pilot.rotateBy(direction * ROTATE_ANGLE * 2); // drehe 10 Grad in neue bevorzugte Richtung
					pilot.drive(TRAVEL_DISTANCE);
					if (sensor.currentLevel < 0.04) {
						sensor.finished = true;
						return;
					}
				}
			}
		}
	}
}

class Sensor implements Runnable {
	float currentLevel;
	boolean darker = true;
	boolean ready = false;

	boolean finished = false;

	@Override
	public void run() {
		EV3ColorSensor pro = new EV3ColorSensor(SearchDarknessBot.LIGHT_SENSOR_PORT);
		SampleProvider sp = pro.getRGBMode();
//		SampleProvider sp = pro.getRedMode();
		float darkest = 1;
		ready = true;
		while (!finished) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			float r = sample[0];
			float g = sample[1];
			float b = sample[2];
			currentLevel = r + b + g;
//			currentLevel = sample[0];
			System.out.println(currentLevel);
			darker = currentLevel < darkest;
			darkest = darker ? currentLevel : darkest;
		}
		pro.close();
	}
}
