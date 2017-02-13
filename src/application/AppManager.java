package application;
import java.util.HashMap;
import java.util.Map;
import gui.*;

public class AppManager{
	private static AppManager currentAppManager;
	private Map<Class<?>,String> snakeTypes = new HashMap<Class<?>, String>();
	private static SnakeManager snakeManager = new SnakeManager();
	//private static GUIController controller = new GUIController();
	private AppManager(){
	}
	private void init(){
		//Initialize and configure the snake.
		AppConfig.addSnakes();
		System.out.println("AppManager Initialized!");
	}
	public static void main(String[] args) {
		currentAppManager = new AppManager();
		currentAppManager.init();
		//Launch GUI
		GUI.run();
	}
	public void addSnakeType(Class<?> snakeClass, String snakeName) {
		snakeTypes.put(snakeClass, snakeName);
	}
	public static AppManager getCurrentAppManager() {
		return currentAppManager;
	}
}
