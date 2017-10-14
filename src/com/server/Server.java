package com.server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JOptionPane;


public class Server {

	private int port;
	private ServerSocket serSocket;
	static HashMap<String, DataOutputStream> clients;

	public Server() throws IOException{
		while(true){
			String temp = JOptionPane.showInputDialog("Enter Port Number : ");
			if(temp == null || temp.equals("")){
				System.out.println("Port Number Not Provided.");
				System.out.println("Server setup aborted");
				break;
			}
			else
				port = Integer.parseInt(temp);
			try {
				serSocket = new ServerSocket(port);
				System.out.println("Server Started...!");
				break;
			} catch (IOException e) {
				System.out.println("Socket initialization failed" +
						"\nPort in use. provide another port number");
			}
		}
		clients = new HashMap<>();

	}

	private void start() throws IOException{
		Socket cSocket = null;
		String name = "";
		BufferedReader br;
		while(true){
			try {
				cSocket = serSocket.accept();
			} catch (IOException e) {
				System.out.println("Failed to Retrieve client socket");
			}
			br  = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			while(!br.ready());
			name = br.readLine();
			System.out.println("Received name : "+ name);
			clients.put(name, new DataOutputStream(cSocket.getOutputStream()));
			clients.get(name).writeBytes("Welcome to Chat Room "+ name + "\n");
			for ( String e : Server.clients.keySet()) {
				if(!(e.equals(name))){
					Server.clients.get(e).writeBytes(name +" Joined the Chat Room" + '\n');
				}

			}
			new Handler(name, br).start();
		}
	}
	public static void main(String[] args) throws IOException {
		Server chatServer = new Server();
		chatServer.start();
	}

}
