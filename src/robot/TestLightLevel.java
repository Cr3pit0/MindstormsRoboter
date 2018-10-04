package robot;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class TestLightLevel {

	static final Port SENSOR_PORT = SensorPort.S1;

	public static void main(String[] args) {
		LightSensor sensor = new LightSensor();
		Thread t = new Thread(sensor);
		t.start();
	}
}

class LightSensor implements Runnable {

	float level;

	@Override
	public void run() {
		EV3ColorSensor pro = new EV3ColorSensor(TestLightLevel.SENSOR_PORT);
		SampleProvider sp = pro.getRedMode();
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			level = sample[0];
			System.out.println(level);
		}
	}
}