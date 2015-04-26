package source;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Connector {

	private int port, packetSize;
	private InetAddress host;
	private DatagramSocket clientSocket;
	private DatagramPacket sendPacket, receivePacket;
	private byte[] sendData, receiveData;
	String clientName;
	
	public Connector(int port, int packetSize, String clientName){
		
		this.port = port;
		this.packetSize = packetSize;
		this.sendData = new byte[packetSize];
		this.receiveData = new byte[packetSize];
		this.clientName = clientName;
		
		try{
			
			clientSocket = new DatagramSocket();
			host = InetAddress.getLocalHost();
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			sendPacket = new DatagramPacket(sendData, sendData.length, host, port);
			
		}
		catch(SocketException | UnknownHostException e){
			
			System.out.println("Error establishing connection with the server.");
			
		}
		
	}
	
	public void sendName(){
		
		try{
			
			sendData = clientName.getBytes("UTF-8");
			sendPacket.setData(sendData);
			sendPacket.setLength(sendData.length);
			clientSocket.send(sendPacket);
		}
		catch(IOException e){
			
			System.out.println("Error sending Client's name to server");
		}
	}
	
	//public 
	
}
