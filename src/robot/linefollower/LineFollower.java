package robot.linefollower;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import robot.Pilot;

public class LineFollower {

	static final float THRESHOLD = .3f;
	static final Port LIGHT_SENSOR_PORT = SensorPort.S1;

	private AmbientLightSensor sensor;
	private Pilot pilot;

	private boolean leftCurve;
	private boolean rightCurve;

	public static void main(String[] args) {
		new LineFollower();
	}

	public LineFollower() {
		leftCurve = false;
		rightCurve = false;
		pilot = new Pilot(80);

		sensor = new AmbientLightSensor();
		Thread t = new Thread(sensor);
		t.start();

		while (true) {
			if (sensor.ready)
				move(sensor.level);
		}
	}

	public void move(float level) {
		if (sensor.level < THRESHOLD - .2 || rightCurve && sensor.level < THRESHOLD) {
			System.out.println("right: " + level);
			rightCurve = true;
			pilot.spinRight();
		} else if (sensor.level > THRESHOLD + .2 || leftCurve && sensor.level > THRESHOLD) {
			System.out.println("left: " + level);
			leftCurve = true;
			pilot.spinLeft();
		} else {
			System.out.println("straight: " + level);
			leftCurve = false;
			rightCurve = false;
			pilot.forward();
		}
	}
}

class AmbientLightSensor implements Runnable {

	float level;
	boolean ready;
	private SampleProvider sp;

	public AmbientLightSensor() {
		ready = false;
	}

	@Override
	public void run() {
		EV3ColorSensor pro = new EV3ColorSensor(LineFollower.LIGHT_SENSOR_PORT);
		sp = pro.getRedMode();
		ready = true;
		while (true) {
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			level = sample[0];
		}
	}
}