package fr.bnancy.midi.server.listener;

public interface ComPortDiscoveredListener {

	public void handlePort(String portName);

	public void handlePortRemoved(String deletedPort);
}
