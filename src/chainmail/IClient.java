package chainmail;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public interface IClient {
	
	String getName();
	ArrayList<Blockchain> getChats();
	String getIPAddress();
	
	default String findIPAddress() {
		try {
			final DatagramSocket socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			String ipAddress = socket.getLocalAddress().getHostAddress();
			return ipAddress;
		} catch(Exception e) {
			return null;
		}
	}
	
	default Thread createClientThread(String ipAddress) {
		Thread clientThread = new Thread() {
			public void run() {
				try {
					Socket socket = new Socket(ipAddress, 9806);
					System.out.println("Connected");
					ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
					Scanner scanner = new Scanner(System.in);
					Block outputBlock = new Block(0, null, scanner.nextLine(), null);
					output.writeObject(outputBlock);
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					Block inputBlock = (Block) input.readObject();
					System.out.println("Message received: " + inputBlock.getMessage());
				} catch(Exception e) {
					e.printStackTrace();
				}

			}
		};
		return clientThread;
	}
	
	default Thread createServerThread() {
		Thread serverThread = new Thread() {
			public void run() {
				try {
					System.out.println("Waiting For Clients");
					ServerSocket serverSocket = new ServerSocket(9806);
					Socket socket = serverSocket.accept();
					System.out.println("Connection Established");
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					Block inputBlock = (Block) input.readObject();
					System.out.println("Message received: " + inputBlock.getMessage());
					ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
					Block outputBlock = new Block(1, null, "Message Recieved", null);
					output.writeObject(outputBlock);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		return serverThread;
	}
}
