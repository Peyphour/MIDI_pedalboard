package fr.bnancy.midi.server.listener;

public interface PacketReceivedListener {

	public void handlePacket(byte[] array);
}
