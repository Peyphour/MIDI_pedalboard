package fr.bnancy.midi;

class Main {
	public static void main(String args[]) {
		MidiApplication app = new MidiApplication();
		app.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				app.stop();
			}
		}));
	}
}
