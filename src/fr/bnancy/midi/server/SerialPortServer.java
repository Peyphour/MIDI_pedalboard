package fr.bnancy.midi.server;

import fr.bnancy.midi.server.listener.ComPortDiscoveredListener;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

public class SerialPortServer {

	private ComPortDiscoveredListener listener;
	private boolean running = true;
	private ArrayList<String> lastPorts = new ArrayList<>(), tmp = new ArrayList<>();
	private SerialPort port = null;

	public SerialPortServer(ComPortDiscoveredListener listener) {
		this.listener = listener;
	}

	public void run() {
		new Thread(() -> {
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
        }).start();
	}

	private void openPort(String portName) {
		CommPortIdentifier portIdentifier = null;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e2) {
			e2.printStackTrace();
		}
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = null;
			try {
				commPort = portIdentifier.open(this.getClass().getName(), 2000);
			} catch (PortInUseException e1) {
				e1.printStackTrace();
			}
			if (commPort instanceof SerialPort) {
				port = (SerialPort) commPort;
				try {
					port.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	private void closePort() {
		if(port == null) {
			System.out.println("port already closed");
			return;
		}
		port.close();
	}

	public SerialPort getSerialPort() {
		return port;
	}

	public void stop() {
		this.running = false;
	}

	public boolean ping(String port)
			throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

		long start;
		
		this.openPort(port);

		if(this.port == null) {
			System.out.println("nul port");
			this.closePort();
			return false;
		}

		InputStream in = this.port.getInputStream();
		OutputStream out = this.port.getOutputStream();

		out.write("PING#".getBytes());
		start = System.currentTimeMillis();

		while (in.available() == 0) {
			if (System.currentTimeMillis() - start > 2000) {
				System.out.println("closed ping timeout");
				this.closePort();
				return false;
			}
		}

		byte[] a = new byte[in.available()];
		in.read(a);

		this.closePort();

		return new String(a).equals("PONG");

	}

	public boolean pingWifi(String port, String ssid, String password, String serverIp) {
		
		try {
			if(!this.ping(port))
				return false;
		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
			e.printStackTrace();
		}
		
		this.openPort(port);
		
		try {
			System.out.println(("WIFITEST#"+ssid+"#"+password+"#"+serverIp));
			this.port.getOutputStream().write(("WIFITEST#"+ssid+"#"+password+"#"+serverIp).getBytes());
			this.port.getOutputStream().flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.closePort();
		
		return false;
	}
}
