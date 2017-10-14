package com.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextArea;


public class Updater extends Thread{

	DataOutputStream bw;
	BufferedReader br;
	JTextArea msgDisplay;
	JFrame frame;
	String myName;
	public Updater(DataOutputStream bw, BufferedReader br, JTextArea msgDisplay, JFrame frame, String myName){
		this.bw = bw;
		this.br = br;
		this.msgDisplay = msgDisplay;
		this.frame = frame;
		this.myName = myName;
	}

	public void run(){
		try {
			bw.writeBytes(myName + '\n');
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			if(br.ready()){
				msgDisplay.append(br.readLine() + "\n");
				msgDisplay.repaint();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true){
			try {
				String temp = null;
				if(br.ready())
					temp = br.readLine();
				if(temp!= null){
					msgDisplay.append(temp + "\n");
					System.out.println("msg in client"+temp);
				}
			} catch (IOException e1) {
				System.out.println("msg retrieval failed in client");
			}
			msgDisplay.repaint();
			frame.repaint();
		}
	}
}
