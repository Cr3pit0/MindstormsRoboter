package robot.canbringer.zwei;

public abstract class Sensor extends Thread {

    private StartupCompletionListener listener;

    protected Sensor(StartupCompletionListener listener) {
        listener.add(this);
        this.listener = listener;
    }

    protected final void startupComplete() {
        listener.setReady(this);
    }
    
    @Override
    public abstract void run();
}
