package robot.findcenter;

import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import robot.Pilot;

public class FindCenter {

    static final Port ULTRASONIC_SENSOR = SensorPort.S2;
    static final Port GYRO_SENSOR = SensorPort.S3;

    public static void main(String[] args) throws InterruptedException {
        Pilot pilot = new Pilot(50);

        GyroSensor gyro = new GyroSensor();
        UltrasonicSensor ultrasonic = new UltrasonicSensor();
        new Thread(gyro).start();
        new Thread(ultrasonic).start();

        while (!(gyro.ready && ultrasonic.ready)) {
            Thread.sleep(1000);
        }
        
        // TEST START
//        for (int i = 0; i < 8; i++) {
//            while (gyro.angle < 90) {
//                pilot.spinRight();
//            }
//            pilot.stop();
//            gyro.reset();
//        }
//        System.exit(0);
        // TEST END

        float[] distance = new float[4];
        for (int i = 0; i < 4; i++) {
            distance[i] = ultrasonic.distance * 100;
            Thread.sleep(2000);
            System.out.println(distance[i]);
            while (gyro.angle < 90) {
                pilot.spinRight();
                System.out.println(gyro.angle);
            }
            pilot.stop();
            gyro.reset();
        }
        // Roboter steht wieder in Ausgangsposition

        // TODO Roboter muss exakt auf der Stelle drehen!
        System.out.println((distance[0] - distance[2]) / 2);
        System.out.println((int) (distance[0] - distance[2]) / 2);
        System.out.println((distance[1] - distance[3]) / 2);
        System.out.println((int) (distance[1] - distance[3]) / 2);

        pilot.drive((int) ((distance[0] - distance[2]) / 2));
        while (gyro.angle < 90) {
            pilot.spinRight();
        }
        pilot.stop();
        pilot.drive((int) ((distance[1] - distance[3]) / 2));
    }
}

class GyroSensor implements Runnable {

    float angle;
    boolean ready;
    private EV3GyroSensor gyroSensor;
    private SampleProvider sp;

    GyroSensor() {
        ready = false;
    }

    void reset() {
        gyroSensor.reset();
    }

    @Override
    public void run() {
        gyroSensor = new EV3GyroSensor(FindCenter.GYRO_SENSOR);
        sp = gyroSensor.getAngleMode();
        gyroSensor.reset();
        ready = true;
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            angle = Math.abs(sample[0]);
        }
    }
}

class UltrasonicSensor implements Runnable {

    float distance;
    boolean ready;
    private SampleProvider sp;

    UltrasonicSensor() {
        ready = false;
    }

    @Override
    public void run() {
        EV3UltrasonicSensor pro = new EV3UltrasonicSensor(FindCenter.ULTRASONIC_SENSOR);
        sp = pro.getDistanceMode();
        ready = true;
        while (true) {
            float[] sample = new float[sp.sampleSize()];
            sp.fetchSample(sample, 0);
            distance = sample[0];
        }
    }
}
