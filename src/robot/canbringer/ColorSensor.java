package robot.canbringer;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

class ColorSensor extends Sensor implements Runnable {

    private volatile float color;
    private EV3ColorSensor colorSensor;
    private SampleProvider sp;

    ColorSensor(StartupCompletionListener listener, Port port) {
        super(listener);
        colorSensor = new EV3ColorSensor(port);
    }

    float getColor() {
        return color;
    }

    @Override
    public void run() {
        sp = colorSensor.getColorIDMode();
        startupComplete();
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            color = sample[0];
        }
    }
}
