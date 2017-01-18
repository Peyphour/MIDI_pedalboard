package fr.bnancy.midi.server.listener;

public interface PacketReceivedListener {

	void handlePacket(byte[] array);
}
