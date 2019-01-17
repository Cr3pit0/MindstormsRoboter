package robot.canbringer.zwei;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

class UltrasonicSensor extends Sensor implements Runnable {

    private volatile float distance;
    private EV3UltrasonicSensor ultrasonicSensor;
    private SampleProvider sp;

    UltrasonicSensor(StartupCompletionListener listener, Port port) {
        super(listener);
        ultrasonicSensor = new EV3UltrasonicSensor(port);
    }

    float getDistance() {
        return distance;
    }

    @Override
    public void run() {
        sp = ultrasonicSensor.getDistanceMode();
        startupComplete();
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            distance = sample[0];
        }
    }
}
