package robot.canbringer.zwei;

import lejos.robotics.subsumption.Behavior;

public class JustDriveForwardBehavior implements Behavior {

    private CanBringer cb;
    private boolean suppressed = false;

    public JustDriveForwardBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        return cb.getUltrasonic().getDistance() > CanBringer.OBSTACLE_DISTANCE;
    }

    @Override
    public void action() {
        suppressed = false;

        while (takeControl()) {
            // cb.getClaw().open();
            // System.out.println("Begin Stupidly Driving Forward...");

            // TODO: beim vergleichen mit Home Color einen Puffer Bereich einbauen !

            // Prüfen ob außerhalb der Basis
            if (cb.isOutOfBase() == false && cb.getHomeColor() != cb.getColor().getColor()) {
                cb.setIsOutOfBase(true);
            }

            // Prüfen ob wieder innerhalb der Basis
            if (cb.isOutOfBase() == true && cb.getHomeColor() == cb.getColor().getColor()) {
                cb.setIsOutOfBase(true);
                cb.rounds -= 1;

                if (cb.rounds == 0) {
                    cb.setIsFinished(true);
                }
            }

            if (!cb.getPilot().isMoving()) {
                cb.getPilot().forward();
            }
        }
    }

    @Override
    public void suppress() {
        cb.getPilot().stop();
        suppressed = true;
    }
}
