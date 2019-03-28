package robot.canbringer.zwei;

import lejos.robotics.subsumption.Behavior;

public class ObstacleLeftBehavior implements Behavior {

    private CanBringer cb;
    private boolean suppressed = false;

    public ObstacleLeftBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        return cb.getTouchLeft().isPressed() && !cb.getTouchRight().isPressed();
    }

    @Override
    public void action() {
        suppressed = false;

        // while (!suppressed || takeControl()) {
        // cb.getPilot().stop();
        // cb.getClaw().open();
        // System.out.println("Obstacle ahead ...");
        System.out.println("left");
        cb.getPilot().travel(-20);

        cb.getPilot().rotate(30);
        // }
    }

    @Override
    public void suppress() {
        cb.getPilot().stop();
        suppressed = true;
    }
}
