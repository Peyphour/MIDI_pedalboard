package fr.bnancy.midi.server.listener;

public interface ServerEventListener {

	void handleServerEvent(String event);
	void connectionExpected();
}
