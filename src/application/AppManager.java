package application;
import java.util.HashMap;
import java.util.Map;
import gui.*;

public class AppManager{
	private static AppManager currentAppManager;
	private Map<Class<?>,String> snakeTypes = new HashMap<Class<?>, String>();
	//private static GUIController controller = new GUIController();
	private AppManager(){
	}
	private void init(){
		//Initialize and configure the snake.
		AppConfig.addSnakes();
		//connectToServer();
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


	public static class Console{
		//private static String[] lines = new String[64];
		//private static int numLines = 0;
		//private static TextArea gui = new TextArea("Welcome!");
		public static void init() {
			//gui.setEditable(false);
			//lines = new String[64];
			//numLines = 0;
		}
		public static void addText(String str) {
			/*
			numLines ++;
			if(numLines == lines.length) {
				for(int i = 0; i < lines.length-1; i ++) {
					lines[i] = lines[i+1];
				}
				numLines--;
			}
			lines[numLines] = str;
			String strings = "";
			for(int i = numLines; i >= 0; i --){
				String s = lines[i];
				if(s != null)	strings += s + "\n";
			}
			gui.setText(strings);
			 */
			System.out.println(str);
		}
	}

}
