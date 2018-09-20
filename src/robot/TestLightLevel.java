package robot;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class TestLightLevel {

    static LightSensor sensor;

    public static void main(String[] args) {
        sensor = new LightSensor(SensorPort.S1);
        Thread t = new Thread(sensor);
        t.start();

        Arbitrator a = new Arbitrator(new Behavior[] { new PrintLightLevel() }, true);
        a.go();
    }
}

class PrintLightLevel implements Behavior {

    private boolean suppressed = false;

    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void suppress() {
        suppressed = true; // standard practice for suppress methods
    }

    @Override
    public void action() {
        suppressed = false;
        while (!suppressed) {
            LCD.drawString("Light level: " + TestLightLevel.sensor.level, 20, 20);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class LightSensor implements Runnable {

    float level;
    private SampleProvider sp;

    public LightSensor(Port port) {
        EV3ColorSensor pro = new EV3ColorSensor(port);
        sp = pro.getAmbientMode();
    }

    @Override
    public void run() {
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            level = (int) sample[0];
        }
    }
}