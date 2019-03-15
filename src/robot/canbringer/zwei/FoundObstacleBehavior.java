package robot.canbringer.zwei;

import lejos.robotics.subsumption.Behavior;

public class FoundObstacleBehavior implements Behavior {

    private CanBringer cb;
    private boolean suppressed = false;

    public FoundObstacleBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        
        return cb.getUltrasonic().getDistance() < CanBringer.OBSTACLE_DISTANCE;
    }

    @Override
    public void action() {
        suppressed = false;

        while (takeControl()) {
            // cb.getPilot().stop();
            // cb.getClaw().open();
            // System.out.println("Obstacle ahead ...");

            cb.getPilot().rotate(30);
        }
    }

    @Override
    public void suppress() {
        cb.getPilot().stop();
        suppressed = true;
    }
}
