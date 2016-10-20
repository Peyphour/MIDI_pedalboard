package fr.bnancy.midi;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.ShortMessage;

import fr.bnancy.midi.Device.TYPE;
import fr.bnancy.midi.server.MidiSocketServer;
import fr.bnancy.midi.server.SerialPortServer;
import fr.bnancy.midi.server.listener.ComPortDiscoveredListener;
import fr.bnancy.midi.server.listener.PacketReceivedListener;
import fr.bnancy.midi.server.listener.ServerEventListener;
import fr.bnancy.midi.util.MidiCommon;
import fr.bnancy.midi.view.MainWindow;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class MidiApplication implements PacketReceivedListener, ServerEventListener, ComPortDiscoveredListener {

	MainWindow mw;
	MidiSocketServer server;
	SerialPortServer comServer;

	ArrayList<Device> devices = new ArrayList<Device>();

	public void start() {

		mw = new MainWindow(this);

		mw.refreshList();
		mw.setSize(740, 430);
		mw.setVisible(true);

		devices.add(new Device(0, -1, TYPE.UNKNOWN, "DIGIT1", false));
		devices.add(new Device(1, -1, TYPE.UNKNOWN, "DIGIT2", false));
		devices.add(new Device(16, -1, TYPE.ANALOG, "EXPR1", false));

		mw.setDevices(devices);

		server = new MidiSocketServer(14123, this, this);
		server.run();

		comServer = new SerialPortServer(this);
		comServer.run();
	}

	public Device searchDevice(int pin) {
		return devices.stream()
				.filter(d -> d.getPin() == pin)
				.findFirst()
				.get();
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

		Device device = searchDevice((int) packet[0] & 0x7F);

		if(device == null ||device.getNumber() == -1)
			return;

		switch(((int)packet[0] & 0xFF) >> 7) {
		case 0:
			MidiCommon.sendMidiMessage(MidiCommon.getReceiverForName(mw.getSelectedMidiDevice(), true), 
					ShortMessage.CONTROL_CHANGE, searchDevice((int) packet[0] & 0x7F).getNumber(), (int) packet[1] & 0xFF);
			break;
		case 1:
			if(device.getType() == TYPE.PRESET_CHANGE)
				MidiCommon.sendMidiMessage(MidiCommon.getReceiverForName(mw.getSelectedMidiDevice(), true), 
						ShortMessage.PROGRAM_CHANGE, device.getNumber(), 0);
			else if(device.getType() == TYPE.CONTROL_CHANGE)
				MidiCommon.sendMidiMessage(MidiCommon.getReceiverForName(mw.getSelectedMidiDevice(), true), 
						ShortMessage.CONTROL_CHANGE, device.getNumber(), ((int) packet[1] & 0xFF) * 0x40);
			else
				System.out.println("Not found");
			break;

		default:
			handleServerEvent("Unsupported");
		}
	}

	public void stop() {
		server.stop();
		comServer.stop();
	}

	@Override
	public void handleServerEvent(String event) {
		mw.addLogMessage(event);
	}

	@Override
	public void handlePort(String portName) {
		mw.addNewComPort(portName);
	}

	@Override
	public void handlePortRemoved(String portName) {
		mw.removePort(portName);
	}

	public boolean pingSerial(String port) {
		try {
			return comServer.ping(port);
		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean pingWifi(String port, String ssid, String password, String serverIp) {
		server.reload();
		server.exceptConnect();
		return comServer.pingWifi(port, ssid, password, serverIp);
	}

	@Override
	public void connectionExpected() {
		mw.pingWifiSuccess();
	}

	public void pingWifiTimeout() {
		server.pingWifiTimeout();
	}
}

