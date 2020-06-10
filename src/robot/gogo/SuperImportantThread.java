package robot.gogo;

import lejos.hardware.Sound;

enum Tones {
	C4(262),
	D4(293),
	E4(330),
	F4(349),
	G4(392),
	A4(440),
	B4f(466),
	B4(494),
	C5(523),
	D5(587),
	E5(659),
	F5(698),
	G5(784),
	A5(880),
	B5s(932),
	C6(1047);
	
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
	PunktierteGanze(24),
	Ganze(16),
	Halbe(8),
	PunktierteViertel(6),
	Viertel(4),
	Achtel(2),
	Sechzehntel(1);
	
	private int factor;
	// Ausgehend von Sechsehnteln aus...
	private int basicDuration = 120;
	
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
    	TeilA();
    	TeilA();
    	TeilB();
    };
    
    public void TeilA() {
    	Sound.playTone(Tones.F4.gib(), Durations.PunktierteViertel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.B4f.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.E4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.F4.gib(), Durations.Viertel.gib());
    	
    	Sound.playTone(Tones.D5.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.B4f.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.F4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Doppelte.gib());
    }
    
    public void TeilB() {
    	Sound.playTone(Tones.G4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.E4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C4.gib(), Durations.Viertel.gib());
    	
    	Sound.playTone(Tones.B4f.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.E4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C4.gib(), Durations.Viertel.gib());
    	
    	Sound.playTone(Tones.C5.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.B4f.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.PunktierteViertel.gib());
    	Sound.playTone(Tones.A4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.B4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.B4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Doppelte.gib());
    	
    	Sound.playTone(Tones.F5.gib(), Durations.PunktierteViertel.gib());
    	Sound.playTone(Tones.E5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.E5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.D5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Viertel.gib());
    	
    	Sound.playTone(Tones.E5.gib(), Durations.PunktierteViertel.gib());
    	Sound.playTone(Tones.D5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.D5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.B4.gib(), Durations.Viertel.gib());
    	
    	Sound.playTone(Tones.A4.gib(), Durations.PunktierteViertel.gib());
    	Sound.playTone(Tones.B4.gib(), Durations.Sechzehntel.gib());
    	Sound.playTone(Tones.C5.gib(), Durations.Sechzehntel.gib());
    	Sound.playTone(Tones.D5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.E5.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.B4f.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.F4.gib(), Durations.Viertel.gib());
    	Sound.playTone(Tones.B4f.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.G4.gib(), Durations.Achtel.gib());
    	Sound.playTone(Tones.F4.gib(), Durations.Ganze.gib());
    }
}

