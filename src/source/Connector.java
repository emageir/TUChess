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
			
			System.out.println(clientName + ": Error establishing connection with the server.");
			
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
			
			System.out.println(clientName + ": Error sending client's name to server");
		}
	}
	
	public String receiveMessages(){
		
		String receivedMsg = null;
		
		try{
			
			clientSocket.receive(receivePacket);
			
			receivedMsg = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
			System.out.println(clientName + ": Received message from server: " + receivedMsg);
			
		}
		catch(IOException e){
			
			System.out.println(clientName + ": Error trying to receive message from server");
		}
		
		return(receivedMsg);
	}
	
	public void sendMessages(String str){
		
		try{
			
			sendData = str.getBytes("UTF-8");
			sendPacket.setData(sendData);
			sendPacket.setLength(sendData.length);
			clientSocket.send(sendPacket);
		}
		catch(IOException e){
			
			System.out.println(clientName + ": Error sending move to server");
		}
	}
	
}
