package robot.canbringer;

import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

public class Claw {

    private RegulatedMotor motor;
    private boolean closed;

    public Claw(Port motor) {
        this.motor = Motor.C;
        closed = true;
    }

    public void open() {
        if (closed) {
            motor.rotate(900, false);
        }
        closed = false;
//        if (closed) {
//            motor.rotate(2000, true);
//        }
//        while (!motor.isStalled()) {
//        }
//        motor.stop();
//        closed = false;
    }

    public void close() {
        if (!closed) {
            motor.rotate(-2000, true);
        }
        while (!motor.isStalled()) {
        }
        motor.stop();
        closed = true;
    }
    
    public void adjust(int angle) {
        motor.rotate(angle);
    }

    public boolean isClosed() {
        return closed;
    }
}
