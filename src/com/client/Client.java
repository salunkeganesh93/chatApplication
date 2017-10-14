package com.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;


public class Client {
	int port;
	String ip;
	Socket socket;
	String myName;
	BufferedReader br;
	DataOutputStream bw;
	Ipscreen ips;
	chatscreen chs;

	public Client(){
		myName = "";
		ips = new Ipscreen();
	}
	
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    static {
        DATE_FORMATTER.setLenient(false);
    }
	public static void main(String[] args) throws ParseException {
		new Client();
		
	}

	class Ipscreen{
		JFrame frame;
		JPanel panel,panel1,panel2,panel3;
		JLabel title,tport,tip,uname;
		JButton connect,send;
		JTextField eport,eip,usname;
		GridBagConstraints c;
		public Ipscreen(){
			frame = new JFrame("Chat Application");
			panel = new JPanel(new GridBagLayout());
			panel1 = new JPanel(new GridLayout(1, 1, 5, 5));
			panel2 = new JPanel(new GridLayout(3, 2, 5, 5));
			panel3 = new JPanel(new GridLayout(1, 1, 5, 5));
			title = new JLabel("Chat Application");
			title.setFont(new Font("calibri", Font.BOLD, 25));
			tport = new JLabel("Enter Port Number : ");
			tport.setFont(new Font("calibri", Font.PLAIN, 17));
			tip = new JLabel("Enter IP : ");
			tip.setFont(new Font("calibri", Font.PLAIN, 17));
			uname = new JLabel("Enter Your Name : ");
			uname.setFont(new Font("calibri", Font.PLAIN, 17));
			eport = new JTextField(10);
			eip = new JTextField(10);
			usname = new JTextField(10);
			connect = new JButton("Connect");
			frame.setSize(330, 300);
			frame.setLocation(500,200);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			panel1.add(title);
			panel2.add(tip);
			panel2.add(eip);
			panel2.add(tport);
			panel2.add(eport);
			panel2.add(uname);
			panel2.add(usname);
			panel3.add(connect);
			
			connect.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					port = Integer.parseInt(eport.getText().toString());
					ip = eip.getText().toString();
					myName = usname.getText().toString();
					try {
						socket = new Socket(ip, port);
						br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						bw = new DataOutputStream(socket.getOutputStream());
						System.out.println("Connection to server Successful.");


					} catch (IOException e1) {
						System.out.println("Connecting to server Failed");
					}

					frame.dispose();
					chs = new chatscreen();
					/*   try {
				    	if(br.ready()){
						abc.msgDisplay.append(br.readLine());
				    	abc.msgDisplay.repaint();
				    	}	
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					new Updater(bw, br, chs.msgDisplay, frame, myName).start();
				}
			});



			c = new GridBagConstraints(0, 1, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			panel.add(panel1, c);
			c = new GridBagConstraints(0, 2, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			panel.add(panel2, c);

			c = new GridBagConstraints(0, 3, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			panel.add(panel3, c);

			frame.getContentPane().add(panel);
			frame.setResizable(false);
			frame.setVisible(true);
		}
	}

	class chatscreen{
		JFrame frame;
		JPanel p, p1, p2, p3, p4,p5;
		JLabel t, sa, ss, ets;
		JTextArea msgDisplay;
		JTextField msg;
		JButton send,logout,reconnect;
		JCheckBox etos;
		GridBagConstraints c;
		Timer act;
		String temp;
		boolean ents;

		public chatscreen(){
			frame = new JFrame("Chat Application");
			frame.setSize(380, 550);
			frame.setLocation(500,100);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			p = new JPanel(new GridBagLayout());
			p1 = new JPanel(new GridLayout(1, 1, 5, 5));
			p2 = new JPanel(new GridLayout(1, 2, 35, 5));
			p3 = new JPanel(new GridLayout(1, 1, 5, 5));
			p4 = new JPanel(new GridBagLayout());
			p5 = new JPanel(new GridLayout(1, 2, 5, 5));

			t = new JLabel("Chat Application");
			t.setFont(new Font("calibri", Font.BOLD, 25));
			sa = new JLabel("Server IP : "+ ip);
			sa.setFont(new Font("calibri", Font.PLAIN, 15));
			ss = new JLabel("        Status : Connected");
			ss.setFont(new Font("calibri", Font.PLAIN, 15));

			msgDisplay = new JTextArea();
			msgDisplay.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(msgDisplay);
			msg = new JTextField(24);
			send = new JButton("Send");
			logout = new JButton("Logout");
			reconnect = new JButton("Reconnect");
			reconnect.setEnabled(false);
			etos = new JCheckBox("Enter to Send");

			p1.add(t);
			p2.add(sa);
			p2.add(ss);
			p3.add(scrollPane);

			c = new GridBagConstraints(0, 1, 2, 1, 1, 1
					, GridBagConstraints.NORTHWEST
					, GridBagConstraints.BOTH
					, new Insets(0, 0, 0, 0), 0, 0);
			p4.add(msg, c);
			msg.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(ents){
						String temp;
						if((temp = msg.getText().toString())!=""){
							try {
								bw.writeBytes(temp + '\n');
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							msgDisplay.append("Me" +": "+temp + "\n");
							msg.setText("");
							frame.repaint();
						}
					}
				}
			});
			c = new GridBagConstraints(3, 1, 1, 1, 1, 1
					, GridBagConstraints.NORTHWEST
					, GridBagConstraints.BOTH
					, new Insets(0, 0, 0, 0), 0, 0);
			p4.add(send, c);
			send.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String temp;
					if((temp = msg.getText().toString())!=""){
						try {
							bw.writeBytes(temp + '\n');
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						msgDisplay.append("Me" +": "+temp + "\n");
						msg.setText("");
						frame.repaint();
					}
				}
			});


			p5.add(logout);
			logout.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						bw.writeBytes("LOGOUT"+ '\n');
					} catch (IOException e1) {
						System.out.println("Logout failed");
					}
					reconnect.setEnabled(true);
					ss.setText("Status : Disconnected");
					logout.setEnabled(false);
					frame.repaint();

				}
			});
			p5.add(reconnect);
			reconnect.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ss.setText("Status : Connected");
					reconnect.setEnabled(false);
					try {
						socket = new Socket(ip, port);
						br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						bw = new DataOutputStream(socket.getOutputStream());
						System.out.println("Connection to server Successful.");
						logout.setEnabled(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					new Updater(bw, br, msgDisplay, frame, myName).start();
				}
			});


			c = new GridBagConstraints(0, 5, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			p.add(etos, c);
			etos.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED)
						ents = true;
					if(e.getStateChange() == ItemEvent.DESELECTED)
						ents = false;
				}
			});


			c = new GridBagConstraints(0, 1, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			p.add(p1, c);
			c = new GridBagConstraints(0, 2, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			p.add(p2, c);

			c = new GridBagConstraints(0, 3, 1, 1, 1, 60
					, GridBagConstraints.CENTER
					, GridBagConstraints.BOTH
					, new Insets(0, 20, 0, 20), 0, 0);
			p.add(p3, c);

			c = new GridBagConstraints(0, 4, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(20, 0, 0, 0), 0, 0);
			p.add(p4, c);

			c = new GridBagConstraints(0, 6, 1, 1, 1, 1
					, GridBagConstraints.CENTER
					, GridBagConstraints.NONE
					, new Insets(0, 0, 0, 0), 0, 0);
			p.add(p5,c);

			frame.getContentPane().add(p);
			frame.setResizable(false);
			frame.setVisible(true);

			/*act  = new Timer(3200, new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						if((temp = br.readLine())!= null)
						 msgDisplay.append(temp + "\n");
					} catch (IOException e1) {
						System.out.println("msg retrieval failed in client");
					}
					frame.repaint();
				}
			});
			act.start();*/
		}
	}
}

