import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) {
		// Read and store the arguments
		// in the variables
		String serverID = args[0];
		int roomPort = Integer.parseInt(args[1]);

		try {
			// Instantiate a new socket for the client
			// using the arguments serverID and port
			Socket userClient = new Socket(serverID, roomPort);
			// Receive data from the server
			InputStream fromServer = userClient.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);
			System.out.println(userClient.getRemoteSocketAddress() + ": " + in.readUTF());

			while(true) {
				// Read the input of the user and send it to the 
				// socket of the server
				OutputStream toServer = userClient.getOutputStream();
				DataOutputStream out = new DataOutputStream(toServer);
				
				// Read input from the user
				BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
				String message = buffer.readLine();
				
				// Exit chat when "exit" is detected
				if(message.equals("exit")) {
					//Send exit message
					out.writeUTF("exit");
					break;
				}
				out.writeUTF(userClient.getLocalSocketAddress() + ": " + message);
			}

			userClient.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}