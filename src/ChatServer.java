import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	// set default port
	private final static int PORT_NUMBER = 4444;
	// lists/arrays for tracking
	private static ArrayList<String> playerNames = new ArrayList<String>();
	private static ArrayList<PrintWriter> broadcasters 
		= new ArrayList<PrintWriter>();
	ServerSocket serverSocket = null;
	
	public ChatServer(String[] args) throws Exception{
        System.out.println("Starting chat server...");
        if (args.length != 1){
			System.out.println("Port Required! " + args.length);
			System.exit(1);
		}
        //also run UDPServer up entry to this chat server
        new UDPServer(Integer.parseInt(args[0]));
        
        try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			while(true) {
				// this handles messages as well as connections
				// also acts as a player thread
				//new ChatHandler(serverSocket.accept()).start();
				new Handler(serverSocket.accept()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// closes the server socket listener
			try{
				serverSocket.close();
			} catch (IOException e) {}
		}
	}
	
	private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        // Handler constructor
        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Requests a unique name from the user
                while (true) {

                    out.println("SUBMITNAME");
                    name = in.readLine();

                    if (name == null) {
                        return;
                    }

                    synchronized (playerNames) {
                        if (!playerNames.contains(name)) {
                            playerNames.add(name);
                            break;
                        }
                    }

                }

                out.println("NAMEACCEPTED");
                broadcasters.add(out);

                while (true) {

                    String input = in.readLine();

                    if (input == null) {
                        return;
                    }

                    for (PrintWriter writer : broadcasters) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }

                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {

                if (name != null) {
                    playerNames.remove(name);
                }

                if (out != null) {
                    broadcasters.remove(out);
                }

                try {
                    socket.close();
                } catch (IOException e) {
                }

            }
        }
    }
}
