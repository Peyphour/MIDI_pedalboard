package fr.bnancy.midi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import fr.bnancy.midi.server.listener.PacketReceivedListener;
import fr.bnancy.midi.server.listener.ServerEventListener;

public class MidiSocketServer {

	private int port;
	private PacketReceivedListener listener;
	private ServerEventListener eventListener = null;
	private boolean running = false;
	private ServerSocket socket;
	private ArrayList<ClientRunnable> clients = new ArrayList<ClientRunnable>();

	public MidiSocketServer(int port, PacketReceivedListener listener, ServerEventListener eventListener) {
		this.port = port;
		this.listener = listener;
		this.eventListener = eventListener;

		try {
			this.socket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		emitEvent("TCP server started on port " + port);
	}

	public void setEventListener(ServerEventListener listener) {
		this.eventListener = listener;
	}

	private void emitEvent(String event) {
		if(this.eventListener != null)
			eventListener.handleServerEvent(event);
	}

	public void run() {

		this.running = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					try {
						Socket client = socket.accept();
						emitEvent("Client connected with IP : " + client.getInetAddress().toString());
						clients.add(new ClientRunnable(client));
						new Thread(clients.get(clients.size() - 1)).start();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

	public void stop() {
		running = false;
		try {
			for(ClientRunnable client : clients) {
				client.running = false;
			}
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ClientRunnable implements Runnable {

		private Socket clientSocket;
		public boolean running = true;

		public ClientRunnable(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public void run() {
			try {
				byte[] array = new byte[2];
				
				while(running) {
					clientSocket.getInputStream().read(array);
				
					listener.handlePacket(array);
				}
				
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
