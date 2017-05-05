package application;

public class AppConfig {
	public static void addSnakes(){
		AppManager.addSnake(new TestSnake());
		AppManager.addSnake(new TestSnake());
	}
}
