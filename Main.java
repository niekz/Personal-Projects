import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Main {
	private static Socket socket = null;
	private static BufferedWriter writer = null;
	private static BufferedReader reader = null;
	private static String channel = "#niekz";
	
	public static void main(String[] args) throws Exception{
		MainWindow window = new MainWindow();
		String server = "irc.freenode.net";
		
//		System.out.println("Written to server");
		
//		writer.write("NICK " + nick + "\r\n");
//		writer.write("USER " + login + " 8 * : New Java Client\r\n");
//		writer.flush();		
		

				
//		writer.write("JOIN " + channel + "\r\n");
//		writer.flush();
		
//		while((line = reader.readLine()) != null){

//		}
	}
	
	public static void writeToServer(String toWrite) throws Exception{
		writeToServer(writer, toWrite);
	}
	
	public static void setSocket(String server){
		try{
			socket = new Socket(server, 6667);
			writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
	        String line = null;
	        while ((line = reader.readLine( )) != null) {
	            if (line.indexOf("004") >= 0) {
	                // We are now logged in.
	                break;
	            }
	            else if (line.indexOf("433") >= 0) {
	                System.out.println("Nickname is already in use.");
	                return;
	            }
	            else
	            	System.out.println(line);
	        }
	        
			System.out.println("Done with opening socket");
			writeToServer("NICK niekz \r\n");
			writeToServer("USER login 8 * : Java Client \r\n");
			writeToServer("JOIN #niekz \r\n");
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static void writeToServer(BufferedWriter x, String toWrite) throws Exception{
		x.write(toWrite);
		x.flush();
	}
}

class CustomOutputStream extends OutputStream{
	private JTextArea textArea;
	
	public CustomOutputStream(JTextArea textArea){
		this.textArea = textArea;
	}
	
	@Override
	public void write(int b) throws IOException{
		textArea.append(String.valueOf((char)b));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}

class MainWindow extends JFrame{
	private JTextField serverField;
	private JTextField messageField;
	private JButton connectButton;
	private JButton sendButton;
	private JTextArea textArea;
	private JList userList;
	
	public MainWindow(){
		super("JIRC Main Window");
		setSize(449, 419);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		setLayout(new GridBagLayout());
		GridBagConstraints bag = new GridBagConstraints();
		
		serverField = new JTextField();
		bag.weightx = 1.0;
		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 0;
		bag.gridy = 0;
		bag.gridwidth = 2;
		bag.ipady = 10;
		add(serverField, bag);
		
		connectButton = new JButton("Connect");
		bag.weightx = 0.0;
		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 2;
		bag.gridy = 0;
		bag.gridwidth = 1;
		bag.ipady = 0;
		add(connectButton, bag);
		

		
		textArea = new JTextArea();

		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		
		System.setOut(printStream);
		System.setErr(printStream);
		
		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 0;
		bag.gridy = 1;
		bag.gridwidth = 2;
		bag.ipady = 300;
		bag.weightx = 1.0;
		add(new JScrollPane(textArea), bag);
		
		userList = new JList<String>();
		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 2;
		bag.gridy = 1;
		bag.ipady = 300;
		bag.weightx = 0.0;
		add(new JScrollPane(userList), bag);
		
		messageField = new JTextField();
		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 0;
		bag.gridy = 2;
		bag.ipady = 10;
		bag.gridwidth = 2;
		bag.weightx = 1.0;
		add(messageField, bag);
		
		sendButton = new JButton("Send");
		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 2;
		bag.gridy = 2;
		bag.gridwidth = 1;
		bag.weightx = 0.0;
		bag.ipady = 0;
		add(sendButton, bag);
		
		serverField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				connectButton.doClick();
			}
		});
		
		messageField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sendButton.doClick();
			}
		});
		
		connectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					Main.setSocket(serverField.getText());
					//Main.writeToServer(serverField.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				serverField.setText("");
			}
		});
		
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					Main.writeToServer(sendButton.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				messageField.setText("");
			}
		});
		
		setSize(650, 420);
		setResizable(false);
	}
}

