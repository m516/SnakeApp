package application;

import java.util.ArrayList;

public class SnakeManager {
	public volatile static ArrayList<Snake> snakes;
	public volatile static ArrayList<ServerBridge> sockets;
	/**
	 * Initializes the SnakeManager instance
	 */
	public SnakeManager() {
		snakes = new ArrayList<Snake>();
		sockets = new ArrayList<ServerBridge>();
		System.out.println("Snake Manager initialized");
	}
	/**
	 * Adds a snake to the list of snakes from this client application
	 * that are currently playing in the arena.
	 * This method automatically binds the snake
	 *  to a port on the server with a new 
	 * <code>ServerBridge</code> instance
	 * @param snake - the <code>Snake</code> instance to add to the arena
	 */
	public synchronized static void addSnake(Snake snake){
		snakes.add(snake);
		ServerBridge socket = new ServerBridge();
		socket.bindToSnake(snake);
		sockets.add(socket);
		System.out.print("Snake added: ");
		System.out.println(snake.toString());
	}
	/**
	 * 
	 * @param index - the index of the snake to retrieve
	 * @return the <code>Snake</code> instance at the location
	 * specified by <i>index</i>
	 */
	public synchronized static Snake getSnake(int index){
		return snakes.get(index);
	}
	/**
	 * Connects all of the <code>ServerBridge</code> instances to a server
	 * @param serverAddress - the IP address of the server
	 * @param port - the port number to connect to
	 */
	public synchronized static void connectSnakesToServer(String serverAddress, int port){
		for(ServerBridge bridge: sockets){
			bridge.connectToServer(serverAddress, port);
		}
	}
	/**
	 * Kills a snake at a certain index
	 * @param index - the index number of the snake to kill
	 */
	public synchronized static void killSnake(int index){
		snakes.get(index).die();
	}
	/**
	 * Moves a snake
	 * @param index
	 * @return
	 */
	public synchronized static String move(int index){
		return "" + snakes.get(index).update();
	}
	public synchronized static void closeAllBridges(){
		for(int i = sockets.size()-1; i >= 0; i --){
			sockets.remove(i).closeSocket();
			snakes.remove(i);
		}
	}
}