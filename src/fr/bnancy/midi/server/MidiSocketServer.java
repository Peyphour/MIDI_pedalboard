package fr.bnancy.midi.server;

import fr.bnancy.midi.server.listener.PacketReceivedListener;
import fr.bnancy.midi.server.listener.ServerEventListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MidiSocketServer {

	private int port;
	private PacketReceivedListener listener;
	private ServerEventListener eventListener = null;
	private boolean running = false;
	private ServerSocket socket;
	private ArrayList<ClientRunnable> clients = new ArrayList<ClientRunnable>();
	private boolean exceptingConnection = false;

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
	
	public void reload() {
		this.stop();
		try {
			this.socket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		emitEvent("TCP server started on port " + port);
		this.run();
	}

	public void run() {

		this.running = true;

		new Thread(() -> {
            while(running) {
                try {
                    Socket client = socket.accept();
                    emitEvent("Client connected with IP : " + client.getInetAddress().toString());
                    if(exceptingConnection) {
                        exceptingConnection = false;
                        eventListener.connectionExpected();
                        emitEvent("Excepted connection done!");
                        client.close();
                    } else {
                        clients.add(new ClientRunnable(client));
                        new Thread(clients.get(clients.size() - 1)).start();
                    }
                } catch (IOException e) {
                    if(!running)
                        break;
                    e.printStackTrace();
                }
            }
        }).start();
	}

	public void stop() {
		emitEvent("Stopping all threads and shutting down the server");
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

	public void exceptConnect() {
		this.exceptingConnection = true;
	}

	public void pingWifiTimeout() {
		this.exceptingConnection = false;
	}

}
