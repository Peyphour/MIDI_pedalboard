package fr.bnancy.midi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import fr.bnancy.midi.server.listener.ComPortDiscoveredListener;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialPortServer {

	private ComPortDiscoveredListener listener;
	private boolean running = true;
	private ArrayList<String> lastPorts = new ArrayList<>(), tmp = new ArrayList<>();

	public SerialPortServer(ComPortDiscoveredListener listener) {
		this.listener = listener;
	}

	public void run() {
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				while (running) {

					Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();

					while (ports.hasMoreElements()) {
						CommPortIdentifier port = ports.nextElement();
						if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
							lastPorts.remove(port.getName());
							tmp.add(port.getName());
							listener.handlePort(port.getName());
						}
					}

					for (String deletedPort : lastPorts)
						listener.handlePortRemoved(deletedPort);
					
					lastPorts.clear();
					lastPorts.addAll(tmp);
					tmp.clear();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void stop() {
		this.running = false;
	}

	public boolean ping(String port)
			throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
		long start;
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				out.write("PING#".getBytes());
				start = System.currentTimeMillis();

				while (in.available() == 0) {
					if (System.currentTimeMillis() - start > 2000)
						return false;
				}

				byte[] a = new byte[4];
				in.read(a);

				out.write("QUIT#".getBytes());

				commPort.close();

				return new String(a).equals("PONG");

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
		return false;
	}

	public boolean pingWifi(String ssid, String password, String serverIp) {
		return false;
	}
}
