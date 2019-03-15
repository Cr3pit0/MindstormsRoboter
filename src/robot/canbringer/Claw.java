package robot.canbringer;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import robot.Level;
import robot.Logger;

public class Claw {

    private RegulatedMotor motor;
    private boolean closed;

    public Claw(Port motor) {
        this.motor = Motor.C;
        closed = false;
    }

    public void open() {
        Logger.log(Level.INFO, "Open claw");
        if (closed) {
            motor.rotate(900, false);
        }
        closed = false;
        Logger.log(Level.INFO, "Claw opened");
//        if (closed) {
//            motor.rotate(2000, true);
//        }
//        while (!motor.isStalled()) {
//        }
//        motor.stop();
//        closed = false;
    }

    public void close() {
        Logger.log(Level.INFO, "Close claw");
        if (!closed) {
            motor.rotate(-2000, true);
            while (!motor.isStalled()) {
            }
            motor.stop();
        }
        closed = true;
        Logger.log(Level.INFO, "Claw closed");
    }
    
    public void adjust(int angle) {
        motor.rotate(angle);
    }

    public boolean isClosed() {
        return closed;
    }
}
