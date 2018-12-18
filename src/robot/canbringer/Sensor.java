package robot.canbringer;

public class Sensor {

    private StartupCompletionListener listener;

    protected Sensor(StartupCompletionListener listener) {
        listener.add(this);
        this.listener = listener;
    }

    protected final void startupComplete() {
        listener.setReady(this);
    }
}
