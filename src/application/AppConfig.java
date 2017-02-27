package application;

public class AppConfig {
	public static void addSnakes(){
		SnakeManager.addSnake(new TestSnake2());
		SnakeManager.addSnake(new TestSnake2());
		System.out.println("AddSnake called");
	}
}
