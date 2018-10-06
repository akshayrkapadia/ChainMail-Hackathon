package chainmail;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
	
	default Thread createClientThread() {
		Thread clientThread = new Thread() {
			public void run() {
				try {
					Socket socket = new Socket("localHost", 9806);
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
}
