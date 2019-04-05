package robot.canbringer.zwei;

import lejos.robotics.subsumption.Behavior;
import robot.Level;
import robot.Logger;

public class StartupBehavior implements Behavior {

    private boolean initialized = false;

    private CanBringer cb;

    public StartupBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        return !initialized;
    }

    @Override
    public void action() {
        Logger.log(Level.INFO, "Initialisierung");
        // cb.getClaw().close();
        // cb.getClaw().open();
        cb.setHomeColor(cb.getColor().getColor());
        cb.getPilot().rotate(-90);
    }

    @Override
    public void suppress() {
    }
}
