package fr.bnancy.midi.util;

import javax.sound.midi.*;
import java.util.ArrayList;

/**
 * Utility methods for MIDI usage.
 */
public class MidiCommon {

	public static ArrayList<String> listDevices(boolean bForInput, boolean bForOutput) {

		ArrayList<String> devices = new ArrayList<String>();

		MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < aInfos.length; i++) {
			try {
				MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
				boolean bAllowsInput = (device.getMaxTransmitters() != 0);
				boolean bAllowsOutput = (device.getMaxReceivers() != 0);
				if ((bAllowsInput && bForInput) || (bAllowsOutput && bForOutput)) {
					devices.add(aInfos[i].getName());
				}
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		if (aInfos.length == 0) {
			devices.add("[No devices available]");
		}
		
		return devices;
	}
	
	public static Receiver getReceiverForName(String name, boolean outputOnly) {
		try {
			MidiDevice device = MidiSystem.getMidiDevice(getMidiDeviceInfo(name, outputOnly));
			device.open();
			return device.getReceiver();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void sendMidiMessage(Receiver recv, int msgType, int data1, int data2) {
		try {
			ShortMessage msg = new ShortMessage();
			msg.setMessage(msgType, data1, data2);
			recv.send(msg, -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	public static MidiDevice.Info getMidiDeviceInfo(String strDeviceName, boolean bForOutput) {
		MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info aInfo : aInfos) {
			if (aInfo.getName().equals(strDeviceName)) {
				try {
					MidiDevice device = MidiSystem.getMidiDevice(aInfo);
					boolean bAllowsInput = (device.getMaxTransmitters() != 0);
					boolean bAllowsOutput = (device.getMaxReceivers() != 0);
					if ((bAllowsOutput && bForOutput) || (bAllowsInput && !bForOutput)) {
						return aInfo;
					}
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
