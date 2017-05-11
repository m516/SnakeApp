package application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServerBridge{
	public static final byte END = 121, ARENA_CONFIG = 122, ARENA_DISPLAY = 123, CLOSE = 124, 
			SNAKE_CONFIG = 125, REQUEST_SNAKE = 126, KILL_SNAKE = 127;
	Socket echoSocket;
	PrintWriter out;
	BufferedReader in;
	String hostAddress = "127.0.0.1";
	int portNumber =6419;
	ScanThread stream = new ScanThread();
	private volatile boolean isLive = false;
	/**
	 * The default constructor for a ServerBridge.
	 */
	public ServerBridge(){
	}
	/**
	 * Binds this instance of ServerBridge to a snake.<p>
	 * This allows the snake to "cross the bridge" between the client and the server.
	 * @param newSnake - the snake that this socket will control
	 */
	public ServerBridge(Snake snake){
		bindToSnake(snake);
	}
	/**
	 * Binds this instance of ServerBridge to a snake.<p>
	 * This allows the snake to "cross the bridge" between the client 
	 * and the server.
	 * @param newSnake - the snake that this socket will control
	 */
	public void bindToSnake(Snake newSnake){
		stream.snake = newSnake;
		System.out.println(stream.snake);
	}
	/**
	 * @return the <code>Snake</code> instance that this bridge is 
	 * synchronized with
	 */
	public Snake getSnake(){
		return stream.snake;
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
			Console.addText("Error parsing this line");
			Console.addText(line);
			e.printStackTrace();
			closeSocket();

		}
		return -1;
	}
	/**
	 * Connects the client application to the server and makes
	 * @param serverAddress - the address of the server to connect to
	 * @param port - the port to connect to
	 */
	public void connectToServer(String serverAddress, int port){
		hostAddress = serverAddress;
		portNumber = port;
		try{ 
			//Object definitions
			echoSocket = new Socket(hostAddress, portNumber);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			String line;
			//Connecting to server using a known socket number 
			//used by the server as a receptor for client applications
			Console.addText("Attempting to connect to " + portNumber);
			writeToServer("Requesting new port number");
			//The server will send an open socket number.
			line = in.readLine();
			//Closing the connection
			Console.addText("Closing connection to " + portNumber);
			closeSocket();
			//Attempting to reconnect to the server using the new socket number
			Console.addText("Attempting to connect to " + line);
			portNumber = new Integer(line);
			//reinitializing the sockets and the print streams
			echoSocket = new Socket(hostAddress, portNumber);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			//Connected! testing connections
			Console.addText("Connected to " + portNumber);
			//writeToServer("Requesting test response");
			//line = in.readLine();
			//Printing the server's response to this successful operation
			//Console.addText("Server: " + line);
			isLive = true;
			//Create a scanner for the new stream
			listenAndParse();
		}
		catch (UnknownHostException e) {
			Console.addText(hostAddress + " does not exist");
			isLive = false;
		} 
		catch (IOException e) {
			Console.addText("Couldn't get I/O for the connection to " + hostAddress + 
					", port number " + portNumber);
			Console.addText(e.getMessage());
			e.printStackTrace();
			isLive = false;
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Listens to the server in a new thread and listens to commands
	 */
	private void listenAndParse(){
		stream.start();
	}

	private void writeToServer(String s){
		try{
			out.println(s);
		}
		catch(Exception e){
			Console.addText(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private String readLine(){
		try {
			String returnString = in.readLine();
			return returnString;
		} catch (IOException e) {
			Console.addText("Error reading a line: " + e.getMessage());
			isLive = false;
			return null;
		}
	}
	private void delay(long milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Console.addText("Error waiting: " + e.getMessage());
		}
	}

	public boolean closeSocket(){
		//Close the socket.  We don't need it any more.
		isLive = false;
		try {
			echoSocket.close();
			Console.addText("Sucessfully closed");
			return true;
		} catch (Exception e) {
			Console.addText("Error closing socket: " + e.getMessage());
			return false;
		}
	}
	public boolean isConnected(){
		return isLive;
	}

	private class ScanThread extends Thread{
		private volatile Snake snake;
		private ScanThread(){
		}
		public synchronized void run(){
			ArrayList<Integer> intList = new ArrayList<Integer>();
			while(isLive){
				int x = getInt();
				while(x!=END){intList.add(x);x=getInt();if(!isLive)return;}
				int command = intList.remove(0);
				Integer[] intArray = new Integer[intList.size()];
				intArray = intList.toArray(intArray);
				switch(command){
				case ARENA_CONFIG:
				case ARENA_DISPLAY:
					Arena.retrieveCommand(command, intArray);
					break;
				case SNAKE_CONFIG:
					//Get the ID of the snake
					snake.setId(intArray[0]);
					Console.addText("ID: "+snake.getId());
					Console.addText("Size: "+(intArray.length-1)/2);
					LocI[] locations = new LocI[(intArray.length-1)/2];
					for(int i = 1; i < intArray.length-1; i += 2){
						locations[(i-1)/2] = new LocI(intArray[i], intArray[i+1]);
					}
					snake.init(locations);
					Console.addText("********Snake initialized");
					for(LocI l: locations){
						Console.addText(l.toString());
					}
					snake.setActive(true);
					break;
				case REQUEST_SNAKE:
					writeToServer("" + snake.update());
					break;
				case CLOSE:
					try {
						echoSocket.close();
						Console.addText("Connection to server successfully closed!");
					} catch (IOException e) {
						Console.addText("Error closing socket: " + e.getMessage());
					}
					isLive = false;
					break;
				case KILL_SNAKE:
					Console.addText("***Your snake is dead :-(");
					snake.die();
					closeSocket();
				}
				intList.clear();
				delay(100);
			}
		}
	}
}
