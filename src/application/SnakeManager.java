package application;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SnakeManager {
	private volatile ObservableList<Snake> snakes = FXCollections.observableArrayList();
	private volatile ArrayList<ServerBridge> sockets;
	private static SnakeManager thisInstance = new SnakeManager();
	/**
	 * Constructs a new instance of SnakeManager
	 */
	public SnakeManager() {
		sockets = new ArrayList<ServerBridge>();
		System.out.println("Snake Manager initialized");
	}
	/**
	 * Adds a snake to the list of snakes contained in the
	 * <code>SnakeManager</code> and binds it to a 
	 * new <code>ServerBridge</code> instance.
	 * @param snake
	 */
	/**
	 * Adds a snake to the list of snakes from this client application
	 * that are currently playing in the arena.
	 * This method automatically binds the snake
	 *  to a port on the server with a new 
	 * <code>ServerBridge</code> instance
	 * @param snake - the <code>Snake</code> instance to add to the arena
	 */
	public synchronized static void addSnake(Snake snake){
		ServerBridge socket = new ServerBridge();
		socket.bindToSnake(snake);
		thisInstance.sockets.add(socket);
		thisInstance.snakes.add(snake);
		System.out.print("Snake added: ");
		System.out.println(snake.toString());
	}
	/**
	 * @param index - the index of the snake
	 * @return the <code>Snake</code> instance at the given
	 * index
	 */
	public synchronized static Snake getSnake(int index){
		return thisInstance.sockets.get(index).getSnake();
	}
	/***
	 * Connects all of the current sockets to a server.
	 * @param serverAddress - the IP address of the server
	 * @param port - the port number to connect to
	 */
	public synchronized static void connectSnakesToServer(String serverAddress, int port){
		for(ServerBridge bridge: thisInstance.sockets){
			bridge.connectToServer(serverAddress, port);
		}
	}
	/**
	 * Moves a single snake
	 * @param index - the index of the <code>Snake</code>
	 * @return the <code>String</code>
	 * representation of the number it returns
	 * @deprecated Sockets already contain this functionality
	 */
	@Deprecated
	public synchronized static String move(int index){
		return "" + thisInstance.sockets.get(index).getSnake().update();
	}
  /**
	 * Closes all of the <code>ServerBridge</code> instances
	 * and removes them from the SnakeManager's list of 
	 * sockets
	 */
	public synchronized static void closeAllBridges(){
		for(int i = thisInstance.sockets.size()-1; i >= 0; i --){
			thisInstance.snakes.remove(thisInstance.sockets.get(i).getSnake());
			thisInstance.sockets.remove(i).closeSocket();
		}
	}
	/**
	 * @return the snake list, used for the GUI manager
	 */
	public static ObservableList<Snake> getSnakes() {
		return thisInstance.snakes;
	}
}