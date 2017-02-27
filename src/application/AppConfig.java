package application;

public class AppConfig {
	public static void addSnakes(){
		SnakeManager.addSnake(new TestSnake());
		//SnakeManager.addSnake(new TestSnake());
		System.out.println("AddSnake called");
	}
}
