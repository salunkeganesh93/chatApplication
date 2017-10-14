package com.server;


import java.io.BufferedReader;
import java.io.IOException;


public class Handler extends Thread{
	private volatile boolean running;
	String cName;
	BufferedReader br;

	public Handler(String name, BufferedReader br2){
		this.br = br2;
		cName = name;
	}
	public void run(){
		String msg;
		System.out.println("Entered handler");
		running = true;
		while(running){
			msg = null;
			try {
				if((msg = br.readLine())!= null){
					if(msg.equals("LOGOUT")){
						System.out.println("Removing Client on request");
						Server.clients.remove(cName);
						for ( String name : Server.clients.keySet()) {
							Server.clients.get(name).writeBytes(cName + " Left the Chat Room " + '\n');
						}
						running = false;
					}
					else{
						for ( String name : Server.clients.keySet()) {
							if(!(name.equals(cName))){
								Server.clients.get(name).writeBytes(cName + " : " + msg + '\n');
							}

						}
						System.out.println("aT server :" + msg);
					}
				}

			} catch (IOException e) {
				System.out.println("Msg read from client failed in handler");
				System.out.println("Removing Client");
				Server.clients.remove(cName);
				for ( String name : Server.clients.keySet()) {
					try {
						Server.clients.get(name).writeBytes(cName + " Left the Chat Room " + '\n');
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
				running = false;
			}

		}
	}
}
