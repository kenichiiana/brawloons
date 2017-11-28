import java.net.*;
import java.io.*;
import java.util.*;

public class UDPServer implements Runnable{
	String playerIN;
	DatagramSocket serverSocket = null;
	int port;
	Thread t = new Thread(this);
	private byte[] in;
	private Map<String, Player> players = new HashMap<String, Player>();
	
	
	
	
	public UDPServer(int inport) {
		this.port = inport;
		
		try {
            serverSocket = new DatagramSocket(port);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+port);
            System.exit(-1);
		}catch(Exception e){}
		
		System.out.println("Server started...");
		
		//Start the server thread
		t.start();
	}
	
	public void broadcast(String msg){ // send string data to all clients
		for(Iterator ite = players.keySet().iterator();ite.hasNext();){ //for each UDPClient send message and other players' info
			String name = (String)ite.next();
			Player player = players.get(name);
			System.out.println("broadcast: "+msg);			
			send(player,msg);
		}
	}
	
	public void send(Player player, String msg){ //send to each player data from server
		DatagramPacket packet;	
		byte out[] = msg.getBytes();		
		packet = new DatagramPacket(out, out.length, player.getAddress(), player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void run() {
		
		while(true) {
			try {
				in = new byte[256];
				
				//receive data from players
				DatagramPacket packet = new DatagramPacket(in, in.length);
				try {
					serverSocket.receive(packet);
				}catch(Exception ioe) {}
				
				playerIN = new String(in);
				System.out.println("rawdata: "+playerIN); //prints none
				playerIN = playerIN.trim();
				
				
				if(playerIN.startsWith("CONNECT"))
				{
					String token[] = playerIN.split(" ");
					Player newPlayer = new Player(token[1], packet.getAddress(), packet.getPort(),Integer.parseInt(token[2]),Integer.parseInt(token[3])); // create new player with data from client
					players.put(token[1].trim(),newPlayer);//add new player to list of players
					broadcast("CONNECTED "+token[1]);
					
				}
					

				if(playerIN.startsWith("PLAYER")){ // parse client data and broadcast to other players
					String token[] = playerIN.split(" ");
										//name 		//x axis		//y axis
					broadcast("PLAYER " +token[1]+" "+token[2]+" "+token[3]);
				}
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}	
	
	}//end of run
	
	public static void main(String args[]){//java UDPServer portnumber
		if (args.length != 1){
			System.out.println("Port Required!");
			System.exit(1);
		}
		
		new UDPServer(Integer.parseInt(args[0]));
	}
}
