package robot.canbringer.zwei;

import java.util.HashSet;
import java.util.Set;

public abstract class StartupCompletionListener {

    private Set<Sensor> sensors;

    StartupCompletionListener() {
        sensors = new HashSet<>();
    }

    void add(Sensor s) {
        sensors.add(s);
    }

    void setReady(Sensor s) {
        sensors.remove(s);
        if (sensors.isEmpty()) {
            onComplete();
        }
    }

    protected abstract void onComplete();
}
