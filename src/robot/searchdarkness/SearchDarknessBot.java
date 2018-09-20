package robot.searchdarkness;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import robot.PilotFactory;

public class SearchDarknessBot {

    static final Port LIGHT_SENSOR_PORT = SensorPort.S1;

    static MovePilot pilot;
    static Sensor sensor;

    public static void main(String[] args) {
        pilot = PilotFactory.generate();

        Thread t = new Thread(sensor);
        t.start();

        Arbitrator a = new Arbitrator(new Behavior[] { new Turn(), new DriveStraight() }, true);
        a.go();
    }
}

class DriveStraight implements Behavior {
    private boolean suppressed = false;

    @Override
    public boolean takeControl() {
        // sensor erkennt Mischung aus weiß und schwarz
        return SearchDarknessBot.sensor.darker;
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void action() {
        suppressed = false;
        while (true) {
            if (suppressed || !SearchDarknessBot.sensor.darker) {
                SearchDarknessBot.pilot.stop();
                break;
            }
            SearchDarknessBot.pilot.forward();
        }
    }
}

class Turn implements Behavior {
    private boolean suppressed = false;
    private int direction = -1; // left : -1, right = 1

    @Override
    public boolean takeControl() {
        return !SearchDarknessBot.sensor.darker;
    }

    @Override
    public void suppress() {
        suppressed = true;
    }

    @Override
    public void action() {
        suppressed = false;
        while (!suppressed) {
            float current = SearchDarknessBot.sensor.currentLevel;
            SearchDarknessBot.pilot.rotate(direction * 5); // drehe 5 Grad in bevorzugte Richtung
            if (SearchDarknessBot.sensor.currentLevel > current) { // falls heller nach Drehung
                direction *= -1; // aendere bevorzugte Richtung
                SearchDarknessBot.pilot.rotate(direction * 10); // Drehe 10 Grad in neue bevorzugte Richtung
            }
        }
    }
}

class Sensor implements Runnable {
    private SampleProvider sp;
    private float darkest = 1;
    float currentLevel;
    boolean darker = true;

    @Override
    public void run() {
        EV3ColorSensor pro = new EV3ColorSensor(SearchDarknessBot.LIGHT_SENSOR_PORT);
        sp = pro.getAmbientMode();
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            currentLevel = sample[0];
            darker = currentLevel < darkest;
            darkest = darker ? currentLevel : darkest;
        }
    }
}
