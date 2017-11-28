import java.net.*;
import java.io.*;
import java.util.Random;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class UDPClient extends Canvas implements Runnable {
	String name;
	String server;
	int x,y;
	int port;
	Thread t = new Thread(this);
	DatagramSocket socket = new DatagramSocket();
	String serverData;
	boolean connected = false;
	//---------------------------------------------UI Things
	private static final long serialVersionUID = 1L;
	
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	private BufferedImage p1,p2,p1b1,p1b2;
	
	private keyboard key;
	
	private Thread thread; 
	private JFrame frame;
	private boolean running = false;

    private Rectangle p1Bounds,p2Bounds,p1b1Bounds,p1b2Bounds;
	
	
	//private Screen screen;
	
	//sets image with buffer
	private BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	//converts image object into array of integer of which pixels receives which color
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	//---------------------------------------end of UI things
	
	
	public UDPClient(String server, String port, String name) throws Exception {
		this.server = server;
		this.name = name;
		this.port = Integer.parseInt(port);
		this.x = 100;
		this.y = 200;
		socket.setSoTimeout(100);
		
		Dimension size = new Dimension(width*scale,height * scale);
		setPreferredSize(size);
		//screen = new Screen(width,height);
		frame = new JFrame();
		
		key = new keyboard();
		
		addKeyListener(key);
		
		
		
	}
	
	public synchronized void start() {
		
		running = true;
		thread = new Thread(this,"Display");
		thread.start();
		
	}
	
	
	public synchronized void stop() {
		try 
		{
			thread.join();
		}
		catch(InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void send(String toServer) { //send data to server
		try{
			//System.out.println("to: "+toServer);
			byte[] out = toServer.getBytes(); //convert string data and send to server data as array of bytes
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(out, out.length, address, port);
        	socket.send(packet);
        }catch(Exception e){}
	}
	
	public void sendToServer(){ //send to server data
		//System.out.print("Enter Message: ");
		//try{
			//BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in)); //sample data sent to server to be broadcast 
			//String message = buffer.readLine();

			send("PLAYER "+name+" "+x+" "+y); // send to server all relevant data as string
		//}catch(IOException e){
			//e.printStackTrace();
		//}
	}
	
	
	public void run() {
		while(running == true) {
			try {
				Thread.sleep(1);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//Get the data from players
			byte[] in = new byte[256];
			DatagramPacket packet = new DatagramPacket(in, in.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){}
			
			serverData = new String(in); //receive byte of data from server then convert to string
			//System.out.println("rawclientdata: "+ serverData);
			serverData = serverData.trim(); //parse String data
			//System.out.println("trimmedclientdata: "+ serverData); 
 
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name+" "+x+" "+y);
			}else if (connected){
				//System.out.println("Connected and Ready to play"); // if connected do game-related tasks
				// GAMEEEEEEEEEEEEE
				update();
				//render();
				//SHOULD RECEIVE ALL MESSAGES
				if (serverData.startsWith("PLAYER")){ //parse data from server
					String[] message = serverData.split(" ");
					System.out.println("Received from:"+message[1]+": "+message[2]+" "+message[3]);
				}
					sendToServer();
			}
						
		}
			
	}
	
	int counter = 0;

	public void update() {
		key.update();
		
		//if(p.balloon1 == true || p.balloon2 == true) {
			if(key.space) { 
				if(counter % 2== 0) {
					
					this.y -= 1;
					
				}
			}
			if(key.left) if(counter % 2 == 0)this.x -= 1;
			if(key.right) if(counter % 2 == 0)this.x += 1;
			
	}

	String bg = "char1Right.png";
	Boolean toggleLeft = true;
	public void render() {
		
		if(counter==0) {
			try {
				p1 = ImageIO.read(getClass().getResource(bg));
				p2 = ImageIO.read(getClass().getResource("char2Right.png"));
				p1b1 = ImageIO.read(getClass().getResource("balloon.png"));
				p1b2 = ImageIO.read(getClass().getResource("balloon.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		BufferStrategy bs = getBufferStrategy();
		
		if(bs == null) {
			
			createBufferStrategy(3);
			return;
		}
		
		
		
		counter++;
		if(counter % 5 == 0 && this.y <= 390) {
			
			this.y++;
		}
		
		if(this.y < 0) {
			
			this.y++;
		}
		
		if(this.x >= 650) {
			
			this.x--;
			
		}
		
		if(this.x<0) {
			
			this.x++;
			
		}
		
		
		
		Graphics g  = bs.getDrawGraphics();
		
		//insert graphics here
		g.setColor(Color.BLACK);
		g.fillRect(0,0,700,getHeight());
		
		//g.drawImage(image, 0, 0, getWidth(), getHeight(),null);
		if(key.left) {
			bg = "char1Left.png";
			
			if(toggleLeft == false) {
				try{p1 = ImageIO.read(getClass().getResource(bg));
				} catch (IOException e) {e.printStackTrace();}
				
				toggleLeft = true;
			}
		}
		
		if(key.right) {
			
			bg = "char1Right.png";
			if(toggleLeft == true) {
				try{p1 = ImageIO.read(getClass().getResource(bg));
				} catch (IOException e) {e.printStackTrace();}
				
				toggleLeft = false;
			}
			
		}
		
	
	
		//for player movement
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(p1,this.x, this.y,this);
		g2d.drawImage(p2,325,175,this);	
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Impact", Font.PLAIN, 20));
		g.drawString("1", this.x+20,this.y+120);
		//g2d.drawImage(p2,0, 340,this);	
		
		p1Bounds = new Rectangle();
		p1Bounds.setSize(52, 100);
		p1Bounds.setLocation(this.x, this.y);

		p1b1Bounds = new Rectangle(this.x-25,this.y-20,48,50); 
		p1b2Bounds = new Rectangle(this.x+25,this.y-20,48,50); 
		//g2d.setColor(Color.WHITE);
		//g2d.fillRect(p.x-25,p.y-20,48,50); 
	
		//if(p.balloon1 == true)
		//if(p.balloon2 == true)	
			g2d.drawImage(p1b1, this.x-25,this.y-20,this);
		
			g2d.drawImage(p1b2, this.x+25,this.y-20,this);
		
		
		p2Bounds = new Rectangle();
		p2Bounds.setSize(52, 100);
		p2Bounds.setLocation(325, 175);
		
		
		
		
		/*
		if (p1Bounds.intersects(p2Bounds)) {	
			if(toggleLeft == true ) p.x++;
			else {
				p.x--;	
			}	
		}
		
		if (p1b1Bounds.intersects(p2Bounds)) {
			p.balloon1 = false;
			
		}
		
		if (p1b2Bounds.intersects(p2Bounds)) {
			p.balloon2 = false;
			
		}
		
		if(p.balloon1 == false && p.balloon2 == false) {
			
			
			//end the game
		}*/
		
		g.dispose();	
		bs.show();
	}
	


	public static void main(String args[]) throws Exception{
		if (args.length != 3){
			System.out.println("Requires serverip, port, and player name.");
			System.exit(1);
		}

		 UDPClient game = new UDPClient(args[0],args[1],args[2]);
		
		//GameUI game = new GameUI();
		
		game.frame.setResizable(false);	
		game.frame.setTitle("Brawloons");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		//Create player
		
		
		
		game.start();
	}
}
