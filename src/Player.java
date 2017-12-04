import java.net.*;

public class Player  {

	public boolean balloon1 = true;
	public boolean balloon2 = true;
	public int y;
	public int x;
	private String name;
	private InetAddress ipaddress;
	private int port;
	public static int counter=0;
	public Player(String usern, InetAddress address, int port,int x,int y) {
		this.name = usern;
		this.ipaddress = address;
		this.port = port;
		this.x = x;
		this.y = y;
		counter++;
	}
	
	public InetAddress getAddress(){
		return ipaddress;
	}

	public int getPort(){
		return port;
	}

	public String getName(){
		return name;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public static int getNumberOfPlayers(){
		
		return counter;
		
	}
}
