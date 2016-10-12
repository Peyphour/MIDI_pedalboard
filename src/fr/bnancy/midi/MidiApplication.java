package fr.bnancy.midi;

import java.util.HashMap;

import javax.sound.midi.ShortMessage;

import fr.bnancy.midi.server.MidiSocketServer;
import fr.bnancy.midi.server.listener.PacketReceivedListener;
import fr.bnancy.midi.server.listener.ServerEventListener;
import fr.bnancy.midi.util.MidiCommon;
import fr.bnancy.midi.view.MainWindow;

public class MidiApplication implements PacketReceivedListener, ServerEventListener {
	
	MainWindow mw;
	MidiSocketServer server;
	
	// Key : Device number (i.e. Pin number on Arduino)
	// Value : value to be sent with MIDI message (program number or control change number)
	HashMap<Integer, Integer> presetChangeDevice = new HashMap<>();
	HashMap<Integer, Integer> controlChangeDevice = new HashMap<>();
	HashMap<Integer, Integer> analogDevice = new HashMap<>(); // i.e. Wah or volume pedal
	
	public void start() {
		mw = new MainWindow();
		mw.refreshList();
		mw.setSize(700, 400);
		
		mw.setVisible(true);
		
		presetChangeDevice.put(0, 1);
		controlChangeDevice.put(1, 2);
		analogDevice.put(16, 5);
		
		server = new MidiSocketServer(14123, this, this);
		server.run();
	}

	@Override
	/**
	 * @param packet
	 * packet[0] : MSB
	 * 		0 => EXPR (Control Change)
	 * 		1 => SWITCH (Either Control Change or Program Change)
	 * packet[0] (7th LSB) : device number (i.e. Pin number on Arduino)
	 * packet[1] : target value (only for control change, ignored for program change)
	 */
	public void handlePacket(byte packet[]) {
		
		System.out.println("New packet");
		System.out.println(((int)packet[0] & 0xFF) >> 7);
		System.out.println((int) packet[0] & 0x7F);
		System.out.println((int) packet[1] & 0xFF);
		
		switch(((int)packet[0] & 0xFF) >> 7) {
		case 0:
			if(analogDevice.containsKey((int) packet[0] & 0x7F))
				MidiCommon.sendMidiMessage(MidiCommon.getReceiverForName(mw.getSelectedMidiDevice(), true), 
						ShortMessage.CONTROL_CHANGE, analogDevice.get((int) packet[0] & 0x7F), (int) packet[1] & 0xFF);
			else
				System.out.println("Not found : analog");
			break;
		case 1:
			if(presetChangeDevice.containsKey((int) packet[0] & 0x7F))
				MidiCommon.sendMidiMessage(MidiCommon.getReceiverForName(mw.getSelectedMidiDevice(), true), 
						ShortMessage.PROGRAM_CHANGE, presetChangeDevice.get((int) packet[0] & 0x7F), 0);
			else if(controlChangeDevice.containsKey((int) packet[0] & 0x7F))
				MidiCommon.sendMidiMessage(MidiCommon.getReceiverForName(mw.getSelectedMidiDevice(), true), 
						ShortMessage.CONTROL_CHANGE, 
						controlChangeDevice.get((int) packet[0] & 0x7F), 
						((int) packet[1] & 0xFF) * 0x40);
			else
				System.out.println("Not found");
			break;
			
		default:
			handleServerEvent("Unsupported");
		}
	}

	public void stop() {
		server.stop();
	}

	@Override
	public void handleServerEvent(String event) {
		mw.addLogMessage(event);
	}
}

