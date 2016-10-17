package fr.bnancy.midi;

import javax.swing.UIManager;

class Main {
	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MidiApplication app = new MidiApplication();
		app.start();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				app.stop();
			}
		}));
	}
}
