package fr.bnancy.midi.server.listener;

public interface ServerEventListener {

	public void handleServerEvent(String event);
	public void connectionExpected();
}
