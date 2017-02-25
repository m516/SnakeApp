package application;

import java.util.ArrayList;

public class SnakeManager {
	public volatile static ArrayList<Snake> snakes;
	public volatile static ArrayList<ServerBridge> sockets;
	public SnakeManager() {
		snakes = new ArrayList<Snake>();
		sockets = new ArrayList<ServerBridge>();
		System.out.println("Snake Manager initialized");
	}
	public synchronized static void addSnake(Snake snake){
		snakes.add(snake);
		ServerBridge socket = new ServerBridge();
		socket.bindToSnake(snake);
		sockets.add(socket);
		System.out.print("Snake added: ");
		System.out.println(snake.toString());
	}
	public synchronized static Snake getSnake(int index){
		return snakes.get(index);
	}
	public synchronized static void connectSnakesToServer(String serverAddress, int port){
		for(ServerBridge bridge: sockets){
			bridge.connectToServer(serverAddress, port);
		}
	}
	public synchronized static void killSnake(int index){
		snakes.get(index).die();
	}
	public synchronized static String move(int index){
		return "" + snakes.get(index).update();
	}
	public synchronized static void closeAllBridges(){
		for(int i = sockets.size()-1; i >= 0; i --){
			sockets.remove(i).closeSocket();
		}
	}
}