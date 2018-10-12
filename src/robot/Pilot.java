package robot;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class Pilot {

	private RegulatedMotor left;
	private RegulatedMotor right;

	private double wheelCircumference;

	private boolean moving;
	private boolean spinningRight;
	private boolean spinningLeft;

	public Pilot(int speed) {
		left = Motor.A;
		right = Motor.D;

		wheelCircumference = 2 * Math.PI * 28;

		left.setSpeed(speed);
		right.setSpeed(speed);

		moving = false;
		spinningRight = false;
		spinningLeft = false;
	}

	public void forward() {
		if (moving) {
			return;
		}
		stop();
		moving = true;
		left.forward();
		right.forward();
	}

	public void backward() {
		if (moving) {
			return;
		}
		stop();
		moving = true;
		left.backward();
		right.backward();
	}

	public void stop() {
		left.stop(true);
		right.stop();
		moving = false;
		spinningLeft = false;
		spinningRight = false;
	}

	public void drive(int distance) {
		if (moving) {
			return;
		}
		stop();
		moving = true;
		left.rotate((int) (360 * (distance / wheelCircumference) * 10), true);
		right.rotate((int) (360 * (distance / wheelCircumference) * 10));
		moving = false;
	}

	public void rotateBy(int angle) {
		if (angle < 0) {
			rotateLeftBy(Math.abs(angle));
		} else {
			rotateRightBy(angle);
		}
	}

	public void rotateRightBy(int angle) {
		if (moving) {
			return;
		}
		stop();
		moving = true;
		left.rotate(angle * 2, true);
		right.rotate(-angle * 2);
		moving = false;
	}

	public void rotateLeftBy(int angle) {
		if (moving) {
			return;
		}
		stop();
		moving = true;
		left.rotate(-angle * 2, true);
		right.rotate(angle * 2);
		moving = false;
	}

	public void spinRight() {
		if (spinningRight) {
			return;
		}
		stop();
		spinningRight = true;
		left.forward();
		right.backward();
	}

	public void spinLeft() {
		if (spinningLeft) {
			return;
		}
		stop();
		spinningLeft = true;
		left.backward();
		right.forward();
	}

	public void setSpeed(int speed) {
		left.setSpeed(speed);
		right.setSpeed(speed);
	}
}
