package application;
import gui.GUI;

public class AppManager{
	private static AppManager currentAppManager;
	private static SnakeManager currentSnakeManager;
	private AppManager(){
	}
	private void init(){
		//Initialize and configure the snake.
		currentSnakeManager = new SnakeManager();
		AppConfig.addSnakes();
		System.out.println("AppManager Initialized!");
	}
	public static void main(String[] args) {
		currentAppManager = new AppManager();
		currentAppManager.init();
		//Launch GUI
		GUI.run();
	}
	/**
	 * @return the current SnakeManager
	 */
	public static SnakeManager getCurrentSnakeManager() {
		return currentSnakeManager;
	}
	/**
	 * Adds a single snake to the current SnakeManager
	 * @param s - the snake to add to the snake manager
	 */
	public static void addSnake(Snake s){
		currentSnakeManager.addSnake(s);
	}
}
