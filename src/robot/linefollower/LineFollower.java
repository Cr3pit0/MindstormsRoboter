package robot.linefollower;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import robot.PilotFactory;

public class LineFollower {

    static final float WHITE_THRESHOLD = .6f;
    static final float BLACK_THRESHOLD = .3f;

    private static final Port LIGHT_SENSOR_PORT = SensorPort.S1;

    static MovePilot pilot;
    static AmbientLightSensor sensor;

    public static void main(String[] args) {
        pilot = PilotFactory.generate();

        sensor = new AmbientLightSensor(LIGHT_SENSOR_PORT);
        Thread t = new Thread(sensor);
        t.start();

        Arbitrator a = new Arbitrator(new Behavior[] { new DriveStraight(), new TurnRight(), new TurnLeft() }, true);
        a.go();
    }
}

class DriveStraight implements Behavior {

    @Override
    public boolean takeControl() {
        return true;
    }

    @Override
    public void suppress() {
        LineFollower.pilot.stop();
    }

    @Override
    public void action() {
        LineFollower.pilot.forward();
    }
}

class TurnLeft implements Behavior {

    private boolean suppressed = false;

    @Override
    public boolean takeControl() {
        return LineFollower.sensor.level > LineFollower.WHITE_THRESHOLD; // sensor erkennt weiﬂ
    }

    @Override
    public void suppress() {
        suppressed = true; // standard practice for suppress methods
    }

    @Override
    public void action() {
        suppressed = false;
        while (!suppressed) {
            LineFollower.pilot.rotate(5);
        }
    }
}

class TurnRight implements Behavior {

    private boolean suppressed = false;

    @Override
    public boolean takeControl() {
        return LineFollower.sensor.level < LineFollower.BLACK_THRESHOLD; // sensor erkennt schwarz
    }

    @Override
    public void suppress() {
        suppressed = true; // standard practice for suppress methods
    }

    @Override
    public void action() {
        suppressed = false;
        while (!suppressed) {
            LineFollower.pilot.rotate(-5);
        }
    }
}

class AmbientLightSensor implements Runnable {

    float level;
    private SampleProvider sp;

    public AmbientLightSensor(Port port) {
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