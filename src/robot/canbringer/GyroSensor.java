package robot.canbringer;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

class GyroSensor extends Sensor implements Runnable {

    private volatile float angle;
    private EV3GyroSensor gyroSensor;
    private SampleProvider sp;

    GyroSensor(StartupCompletionListener listener, Port port) {
        super(listener);
        gyroSensor = new EV3GyroSensor(port);
    }

    float getAngle() {
        return angle;
    }

    void reset() {
        gyroSensor.reset();
    }

    @Override
    public void run() {
        sp = gyroSensor.getAngleMode();
        gyroSensor.reset();
        startupComplete();
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            angle = Math.abs(sample[0]);
        }
    }
}