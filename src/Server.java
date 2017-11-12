import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
   public static ServerSocket serverSocket;
    
    public static void acceptClients(){
	    //Create an arraylist of clients
        ArrayList<ClientThread> clients =  new ArrayList<ClientThread>();
        
        System.out.println("Waiting for Clients");
	    //continue accepting clients
        while(true){
            try {
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(socket);
		        //Create threads for each client
                Thread t = new Thread(client);
                t.start();
                clients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] args){
    int port = Integer.parseInt(args[0]);
    try {
      serverSocket = new ServerSocket(port);
      acceptClients();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
