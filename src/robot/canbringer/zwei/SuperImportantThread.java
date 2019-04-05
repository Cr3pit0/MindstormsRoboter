package robot.canbringer.zwei;

import lejos.hardware.Sound;

enum Tones {
	C(262),
	D(293),
	E(330),
	F(349),
	G(392),
	A(440),
	H(494),
	C5(523);
	
	private int frequency;
	
	Tones(int _frequency){
		this.frequency = _frequency;
	}
	
	public int gib(){
		return this.frequency;
	}
}

enum Durations {
	Doppelte(32),
	Ganze(16),
	Halbe(8),
	Viertel(4),
	Achtel(2),
	Sechzehntel(1);
	
	private int factor;
	// Ausgehend von Sechsehnteln aus...
	private int basicDuration = 200;
	
	Durations(int _factor){
		this.factor = _factor;
	}
	
	public int gib(){
		return this.basicDuration * this.factor;
	}
}


class SuperImportantThread extends Thread {
	
    protected SuperImportantThread() {}
    
    @Override
    public void run() {
    	Sound.playTone(Tones.A.gib(), Durations.Doppelte.gib());
    	Sound.playTone(Tones.G.gib(), Durations.Doppelte.gib());
    	Sound.playTone(Tones.A.gib(), Durations.Doppelte.gib());
    	Sound.playTone(Tones.H.gib(), Durations.Doppelte.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Doppelte.gib());
    	Sound.playTone(Tones.H.gib(), Durations.Doppelte.gib());
    	Sound.playTone(Tones.A.gib(), Durations.Doppelte.gib());
    };
}
