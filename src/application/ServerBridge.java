package application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServerBridge{
	public static final int END = -2, ARENA_CONFIG = -3, ARENA_DISPLAY = -4, CLOSE = -5, 
			SNAKE_CONFIG = -6, REQUEST_SNAKE = -7, KILL_SNAKE = -8;
	Socket echoSocket;
	PrintWriter out;
	BufferedReader in;
	String hostAddress = "127.0.0.1";
	int portNumber =2060;
	private volatile boolean isLive = true;
	public static void main(String[] args){
		ServerBridge b = new ServerBridge();
		b.connectToServer();
	}
	public ServerBridge(){
	}
	public ServerBridge(String serverAddress, int port){
		hostAddress = serverAddress;
		portNumber = port;
	}
	/**
	 * Retrieves an integer value from an application
	 * @return the next number that the client application sent to the server
	 */
	private int getInt(){
		String line = "no text in this line";
		try { 
			line = in.readLine();
			int r = Integer.parseInt(line);
			return r;
		} catch (NumberFormatException | IOException e) {
			AppManager.Console.addText("Error parsing this line");
			AppManager.Console.addText(line);
			closeSocket();
			
		}
		return -1;
	}
	public void connectToServer(){
		try{ 
			//Object definitions
			echoSocket = new Socket(hostAddress, portNumber);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			String line;
			//Connecting to server using a known socket number 
			//used by the server as a receptor for client applications
			AppManager.Console.addText("Attempting to connect to " + portNumber);
			writeToServer("Requesting new port number");
			//The server will send an open socket number.
			line = in.readLine();
			//Closing the connection
			AppManager.Console.addText("Closing connection to " + portNumber);
			closeSocket();
			//Attempting to reconnect to the server using the new socket number
			AppManager.Console.addText("Attempting to connect to " + line);
			portNumber = new Integer(line);
			//reinitializing the sockets and the print streams
			echoSocket = new Socket(hostAddress, portNumber);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			//Connected! testing connections
			AppManager.Console.addText("Connected to " + portNumber);
			writeToServer("Requesting test response");
			line = in.readLine();
			//Printing the server's response to this successful operation
			AppManager.Console.addText("Server: " + line);
			isLive = true;
			//Create a scanner for the new stream
		} 
		catch (UnknownHostException e) {
			AppManager.Console.addText(hostAddress + " does not exist");
		} 
		catch (IOException e) {
			AppManager.Console.addText("Couldn't get I/O for the connection to " + hostAddress + 
					", port number " + portNumber);
		}
	}


	void sendData(int dataCode, String[] data){
	}

	public void listenAndParse(){
		ScanThread stream = new ScanThread();
		stream.start();
	}

	private void writeToServer(String s){
		try{
			out.println(s);
		}
		catch(Exception e){
			AppManager.Console.addText(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private String readLine(){
		try {
			String returnString = in.readLine();
			return returnString;
		} catch (IOException e) {
			AppManager.Console.addText("Error reading a line: " + e.getMessage());
			isLive = false;
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	private void delay(long milliseconds){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			AppManager.Console.addText("Error waiting: " + e.getMessage());
		}
	}

	public boolean closeSocket(){
		//Close the socket.  We don't need it any more.
		isLive = false;
		try {
			echoSocket.close();
			AppManager.Console.addText("Sucessfully closed");
			return true;
		} catch (Exception e) {
			AppManager.Console.addText("Error closing socket: " + e.getMessage());
			return false;
		}
	}
	public boolean isConnected(){
		return isLive;
	}

	private class ScanThread extends Thread{
		private ScanThread(){
		}
		public void run(){
			ArrayList<Integer> intList = new ArrayList<Integer>();
			while(isLive){
				int currentInt = getInt();
				intList.add(currentInt);
				if(currentInt == END){
					int command = intList.get(0);
					intList.remove(0);
					intList.remove(intList.size()-1);
					Integer[] intArray = new Integer[intList.size()];
					intArray = intList.toArray(intArray);
					switch(command){
					case ARENA_CONFIG:
					case ARENA_DISPLAY:
					case SNAKE_CONFIG:
						Arena.retrieveCommand(command, intArray);
						break;
					case REQUEST_SNAKE:
						writeToServer(Arena.move());
						break;
					case CLOSE:
						try {
							echoSocket.close();
							AppManager.Console.addText("Connection to server successfully closed!");
						} catch (IOException e) {
							AppManager.Console.addText("Error closing socket: " + e.getMessage());
						}
						isLive = false;
						break;
					case KILL_SNAKE:
						AppManager.Console.addText("***Your snake is dead :-(");
						Arena.killSnake();
					}
					intList.clear();
				}
			}
		}
	}
}
