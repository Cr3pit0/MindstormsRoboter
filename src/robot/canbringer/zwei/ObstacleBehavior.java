package robot.canbringer.zwei;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
import robot.gogo.GoGoBot;

public class ObstacleBehavior implements Behavior {

    private CanBringer cb;
    private boolean suppressed = false;

    public ObstacleBehavior(CanBringer cb) {
        this.cb = cb;
    }

    @Override
    public boolean takeControl() {
        return cb.getTouchLeft().isPressed() || cb.getTouchRight().isPressed();
    }

    @Override
    public void action() {
        suppressed = false;
        
        int DISTANCE = 30;
        int RADIUS = 45;
        int drive = 0;
        
        if(cb.getTouchLeft().isPressed() && !cb.getTouchRight().isPressed()) {
        	drive = 1;
        }
        else if(!cb.getTouchLeft().isPressed() && cb.getTouchRight().isPressed()) {
        	drive = 2;
        }
        		
    	switch(drive) {
    		case 0: 
				cb.getPilot().travel(-DISTANCE);
				cb.getPilot().arcBackward(-RADIUS);
				break;
    		case 1: 
    			cb.getPilot().travel(-DISTANCE);
				cb.getPilot().arcBackward(RADIUS);
    			break;
    			
    		case 2: 
    			cb.getPilot().travel(-DISTANCE);
				cb.getPilot().arcBackward(-RADIUS);
    			break;
    			
			default:
				cb.getPilot().travel(-DISTANCE);
				break;
    	}
        
    }

    @Override
    public void suppress() {
        cb.getPilot().stop();
        suppressed = true;
    }
}
