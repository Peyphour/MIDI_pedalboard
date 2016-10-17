package fr.bnancy.midi.server;

import java.util.Enumeration;

import fr.bnancy.midi.server.listener.ComPortDiscoveredListener;
import gnu.io.CommPortIdentifier;

public class SerialPortServer {

	private ComPortDiscoveredListener listener;
	private boolean running = true;
	
	public SerialPortServer(ComPortDiscoveredListener listener) {
		this.listener = listener;
	}
	
	// TODO check deleted ports
	public void run() {
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				while(running) {
					
					Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
					
			        while (ports.hasMoreElements()) {
			            CommPortIdentifier port = ports.nextElement();
			            if(port.getPortType() == CommPortIdentifier.PORT_SERIAL)
			            	listener.handlePort(port.getName());
			        } 
			        
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
}
