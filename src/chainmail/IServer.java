package chainmail;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Base64;

public interface IServer {
	
	String getServerID();
	
	default String createServerID(String name, String ipAddress) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest((name + ipAddress).getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(hash);
		} catch(Exception e) {
			return null;
		}
		
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
