package fr.bnancy.midi;

import javax.swing.UIManager;

public class Main {
	public static void main(String args[]) {

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MidiApplication app = new MidiApplication();
		app.start();

		Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
	}
}
