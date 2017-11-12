import java.net.*;
import java.io.*;

public class ClientThread extends Server implements Runnable{
    private Socket server;

    public ClientThread(Socket socket){
        this.server = socket;
    }

    public void run(){
        try {
			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

			/* Start accepting data from the ServerSocket */
			System.out.println("Just connected to " + server.getRemoteSocketAddress());

			/* Read data from the ClientSocket */
			DataOutputStream out = new DataOutputStream(server.getOutputStream());
			out.writeUTF("Connection acknowledged.");

			while(server.getInputStream()!=null){
				DataInputStream in = new DataInputStream(server.getInputStream());

				String message = in.readUTF().toString();
				System.out.println(message);
				if(message.equals("exit")){
					break;
				}
			}
        server.close();
      }catch(IOException e){
        e.printStackTrace();
      }
    }
}
