package application;

import java.util.ArrayList;

public class SnakeManager {
	public static ArrayList<Snake> snakes;
	public static ArrayList<ServerBridge> sockets;
	public SnakeManager() {
		snakes = new ArrayList<Snake>();
		sockets = new ArrayList<ServerBridge>();
	}
	public static void addSnake(Snake snake, int id){
		snake.setID(id);
		snakes.add(snake);
		ServerBridge socket = new ServerBridge();
		sockets.add(socket);
	}
	public static void addSnake(Snake snake){
		snakes.add(snake);
		ServerBridge socket = new ServerBridge();
		sockets.add(socket);
	}
	public static Snake getSnake(int index){
		return snakes.get(index);
	}
	public static void connectSnakesToServer(String serverAddress, int port){
		for(ServerBridge bridge: sockets){
			bridge.connectToServer(serverAddress, port);
		}
	}
	public static void killSnake(int index){
		snakes.get(index).die();
	}
	public static String move(int index){
		return "" + snakes.get(index).update();
	}
}
