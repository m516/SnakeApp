package application;

public class AppManager{
	//private static AppManager currentAppManager;
	private static SnakeManager currentSnakeManager;
	private static SnakeConfigurationManager currentSnakeConfigurationManager;
	private AppManager(){
	}
	public static void init(){
		//Initialize and configure the snake.
		currentSnakeManager = new SnakeManager();
		System.out.println("AppManager Initialized!");
	}
	public static void main(String[] args) {
		//currentAppManager = new AppManager();
		AppManager.init();
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
	 * Adds a single snake to the current SnakeManager. <p>
	 * <b>WARNING!</b> This must ONLY be used by a 
	 * <code>SnakeConfigurationManager</code> instance
	 * to successfully add snakes to the application
	 * @param s - the snake to add to the snake manager
	 */
	public static void addSnake(Snake s){
		if(currentSnakeManager==null){
			System.err.println("You must initializeAppManager first!");
			System.err.println("Initializing now");
			init();
		}
		currentSnakeManager.addSnake(s);
	}
	
	/**
	 * Adds all snakes to the SnakeManager
	 * @param currentSnakeConfigurationManager
	 */
	static void addSnakesToArena(){
		if(currentSnakeConfigurationManager==null){
			System.err.println("Use setSnakeConfigurationManager() to add snakes to the application!");
		}
		else{
			currentSnakeConfigurationManager.addSnakes();
		}
	}
	
	/**
	 * Sets the current configuration manager, the class that 
	 * allows users to add their snakes to the game
	 */
	public static void setSnakeConfigurationManager(SnakeConfigurationManager newManager){
		currentSnakeConfigurationManager = newManager;
	}
}
