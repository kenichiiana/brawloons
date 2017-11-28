import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;	
import java.awt.event.*;

public class Chat {
	BufferedReader receive;
	PrintWriter broadcast;
	
	JFrame frame = new JFrame("Brawloons Chat!");
	JTextField textField = new JTextField(30);
	JTextArea messageArea = new JTextArea(20,30);
	String name;
	String server;
	
	public static void main(String[] args) {
		Chat client = new Chat(args[0], args[1]);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		try{
			client.run();
		} catch (IOException e) {}
	}
	
	// ui constructor
	public Chat(String server, String playerName) {
		this.name = playerName;
		this.server = server;
		
		// set gui parameters
		textField.setEditable(true);
		System.out.println("YO");
		textField.setBorder(BorderFactory.createCompoundBorder(textField.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
		messageArea	.setEditable(false);
		messageArea.setMargin(new Insets(10, 10, 10, 10));
		messageArea.append("BRAWLOONS!!!\n\n\n");
		frame.getContentPane().add(textField, "South");
		frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		frame.pack();
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				broadcast.println(textField.getText());
				textField.setText("");
			}
		});
	}
	
	public void run() throws IOException {
		try  {	
			String serverAddress = this.server;
			Socket socket = new Socket(serverAddress, 4444);
		
			receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			broadcast = new PrintWriter(socket.getOutputStream(), true);
		
			while(true) {
				String line = receive.readLine();
			
				if(line.startsWith("SUBMITNAME")) {
					broadcast.println(this.name);
				} else if (line.startsWith("NAMEACCEPTED")) {
					textField.setEditable(true);
				} else if (line.startsWith("MESSAGE")) {
					messageArea.append(line.substring(8) + "\n");
				}
			}
		} catch (IOException e) {}
	}
}
