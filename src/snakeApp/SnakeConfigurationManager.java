/**
 * 
 */
package snakeApp;

/**
 * An interface that allows users to add snakes to
 * the application so that they can connect to an 
 * online arena.
 * @author Micah Mundy
 *
 */
public interface SnakeConfigurationManager {
	/**
	 * This method, called by AppManager, adds snakes
	 * to the application, allowing the snakes to compete online.
	 * To use this method, call AppManager.addSnakes(Snake s);
	 */
	public void addSnakes();
}
