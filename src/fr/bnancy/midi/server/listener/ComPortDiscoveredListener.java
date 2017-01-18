package fr.bnancy.midi.server.listener;

public interface ComPortDiscoveredListener {

	void handlePort(String portName);

	void handlePortRemoved(String deletedPort);
}
