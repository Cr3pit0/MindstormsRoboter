package robot.canbringer.zwei;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

class TouchSensor extends Sensor implements Runnable {

    private volatile boolean pressed;
    private EV3TouchSensor touchSensor;
    private SampleProvider sp;

    TouchSensor(StartupCompletionListener listener, Port port) {
        super(listener);
        touchSensor = new EV3TouchSensor(port);
    }

    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void run() {
        sp = touchSensor.getTouchMode();
        startupComplete();
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            pressed = sample[0] == 1;
        }
    }
}
