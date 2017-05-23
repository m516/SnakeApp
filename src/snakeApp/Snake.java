package snakeApp;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The <i>SnakeRoot</i> class is an abstract class that users can extend to make
 * snakes that compete online.  It includes basic functions that most snakes
 * need to stay alive, find food, track other players, etc. <p>
 * <b>NOTE:</b> modifying this class in any way does not affect the overall
 * outcome of the game.  All of the information regarding games, snakes
 * and arenas is contained in the main server.  For your benefit, 
 * please do not modify this class or any class contained in this repository
 * unless you know what you're doing.
 * @author Micah Mundy
 *
 */
public abstract class Snake {
	private boolean active = false;
	private boolean dead = false;
	protected static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3, DEAD = 4;
	protected static final int OUTOFBOUNDS = 0, EMPTY = 1, WALL = 2, FRUIT = 3;
	int steps = 0;	
	private ArrayList<LocI> segs = new ArrayList<LocI>();
	private final SimpleStringProperty name = new SimpleStringProperty(name());
	private final SimpleIntegerProperty id = new SimpleIntegerProperty(-1);
	public Snake(){};
	/**
	 * The constructor for a snake
	 * @param location - the initial location of the 
	 * @param numSegs - the number of segments initially on the snake
	 */
	public Snake(LocI location, int numSegs) {
		for(int i = 0; i < numSegs; i ++){
			segs.add(location.clone());
		}
	}
	/**
	 * The most commonly used constructor for snakes
	 * @param locations - the initial locations for all of the snake segments
	 */
	public void init(LocI[] locations){
		for(LocI loc: locations){
			segs.add(loc);
		}
	}
	/**
	 * This method should be overridden by the person writing the code
	 * for his/her snake.  It should return one of the direction constants
	 * defined in this class, which are <i>DOWN</i>, <i>RIGHT</i>, <i>UP</i>, and <i>LEFT</i>.
	 * <p> If the value returned is not equal to any of those constants, 
	 * the snake will default to moving left and will die if it 
	 * encounters anything besides an empty space or fruit.
	 * <p> <i>DOWN</i> changes the y-value of the head by -1.
	 * <p> <i>UP</i> changes the y-value of the head by +1.
	 * <p> <i>LEFT</i> changes the x-value of the head by -1.
	 * <p> <i>RIGHT</i> changes the x-value of the head by +1.
	 * @return the direction of the snake, programmed by players
	 */
	public abstract int move();
	/** 
	 * @return the name of the snake
	 */
	protected abstract String name();
	/**
	 * This method updates the snake's position of the <i>move</i> method.
	 * It should not be called by snakes.
	 */
	final public int update() {
		if(!dead && active){
			steps++;
			int dir = DEAD;
			try{
				dir = move();
			}
			catch(Exception e){
				Console.addText("**********************************");
				Console.addText("ERROR in snake code:");
				Console.addText(e.getMessage());
				StackTraceElement[] lines = e.getStackTrace();
				for (StackTraceElement stackTraceElement : lines) {
					Console.addText(stackTraceElement.toString());
				}
				Console.addText("This snake DIED!!!");
				Console.addText("**********************************");
				die();
				return DEAD;
			}

			//Update the snake segments
			for (int i = segs.size()-1; i > 0; i--) {
				segs.get(i).jumpTo(segs.get(i-1));
			}
			if(dir == DOWN) segs.get(0).translate(0, 1);
			else if(dir == UP) segs.get(0).translate(0, -1);
			else if(dir == RIGHT) segs.get(0).translate(1, 0);
			else if(dir == LEFT) segs.get(0).translate(-1, 0);
			return dir;
		}

		return DEAD;
	}
	/**
	 * @return true if the snake can go right without dying in the process
	 */
	final protected boolean canGoRight(){
		LocI h = getHead();
		h.translate(1, 0);
		return isGood(h);
	}
	/**
	 * @return true if the snake can go left without dying in the process
	 */
	final protected boolean canGoLeft(){
		LocI h = getHead();
		h.translate(-1, 0);
		return isGood(h);
	}
	/**
	 * @return true if the snake can go up without dying in the process
	 */
	final protected boolean canGoUp(){
		LocI h = getHead();
		h.translate(0, -1);
		return isGood(h);
	}
	/**
	 * @return true if the snake can go down without dying in the process
	 */
	final protected boolean canGoDown(){
		LocI h = getHead();
		h.translate(0, 1);
		return isGood(h);
	}
	/**
	 * @param l - a location
	 * @return true if the snake can survive at location <i>l</i>.
	 */
	final protected boolean isGood(LocI l){
		if(isWall(l)||!isInBounds(l)||isSnake(l)!=-1) return false;
		return true;
	}
	/**
	 * @param direction - the current (or planned) direction of the snake
	 * @return the location of the snake's head after it moves
	 * <p> NOTE: the position will return <i>null</i> if <i>direction</i>
	 * is not a number that specifies a legitimate direction.
	 */
	final protected LocI getNextHeadPos(int direction){
		LocI loc = getHead();
		if(direction == DOWN) loc.translate(0, 1);
		else if(direction == UP) loc.translate(0, -1);
		else if(direction == RIGHT) loc.translate(1, 0);
		else if(direction == LEFT) loc.translate(-1, 0);
		else return null;
		return loc;
	}
	/**
	 * This method returns the state of the position specified in the parameter.
	 * @param x - the x-position of the location
	 * @param y - the y-position of the location
	 * @return an integer constant representing one of the following states:
	 * <p> <i>OUTOFBOUNDS</i>: the position is not in the playing field.
	 * <p> <i>EMPTY</i>: an empty cell
	 * <p> <i>WALL</i>: a cell containing a wall
	 * <p> <i>SNAKE</i>: a cell containing a snake segment
	 * <p> <i>FRUIT</i>: a cell containing a fruit
	 */
	final protected int getBlockTypeAtLocation(int x, int y) {
		return Arena.getBlock(x, y);
	}	
	/**
	 * This method returns the state of the position specified in the parameter.
	 * @param location - the location to be tested
	 * @return an integer constant representing one of the following states:
	 * <p> <i>OUTOFBOUNDS</i>: the position is not in the playing field.
	 * <p> <i>EMPTY</i>: an empty cell
	 * <p> <i>WALL</i>: a cell containing a wall
	 * <p> <i>SNAKE</i>: a cell containing a snake segment
	 * <p> <i>FRUIT</i>: a cell containing a fruit
	 */
	final protected int getBlockTypeAtLocation(LocI location) {
		return Arena.getBlock(location.getX(), location.getY());
	}	
	/**
	 * @param x - the x-position of the location
	 * @param y - the y-position of the location
	 * @return true if the location is in bounds
	 */
	final protected boolean isInBounds(int x, int y) {
		if(x<0) return false;
		if(y<0) return false;
		if(x>=Arena.getXSize()) return false;
		if(y>=Arena.getYSize()) return false;
		return true;
	}
	/**
	 * @param l - the location
	 * @return true if the location is in bounds
	 */
	final protected boolean isInBounds(LocI l) {
		return isInBounds(l.getX(),l.getY());
	}
	/**
	 * @param x - the x-position of the location
	 * @param y - the y-position of the location
	 * @return true if the location is blocked by a wall
	 */
	final protected boolean isWall(int x, int y) {
		if(isInBounds(x,y))
			return getBlockTypeAtLocation(x, y) == WALL;
		else return false;
	}
	/**
	 * @param l - the location
	 * @return true if the location is blocked by a wall
	 */
	final protected boolean isWall(LocI l) {
		if(isInBounds(l))
			return isWall(l.getX(),l.getY());
		else return false;
	}
	/**
	 * @param x - the x-position of the location
	 * @param y - the y-position of the location
	 * @return true if the location is a fruit
	 */
	final protected boolean isFruit(int x, int y) {
		if(isInBounds(x,y))
			return getBlockTypeAtLocation(x, y) == FRUIT;
		else return false;
	}
	/**
	 * @param l - the location
	 * @return true if the location is a fruit
	 */
	final protected boolean isFruit(LocI l) {
		if(isInBounds(l))
			return isFruit(l.getX(),l.getY());
		else return false;
	}
	/**
	 * @param x - the x-position of the location
	 * @param y - the y-position of the location
	 * @return the snake ID if the location is blocked by a snake
	 * and <i>-1</i> if a snake does not currently occupy
	 * the block in question
	 */
	final protected int isSnake(int x, int y) {
		int i = getBlockTypeAtLocation(x, y);
		if(i > FRUIT) return i-1-FRUIT;
		return -1;
	}
	/**
	 * @param l - the location
	 * @return the snake ID if the location is blocked by a snake
	 * and <i>EMPTY</i> if a snake does not currently occupy
	 * the block in question
	 */
	final protected int isSnake(LocI l) {
		return isSnake(l.getX(),l.getY());
	}
	/**
	 * This method kills the snake
	 * <p>
	 * Note: there should be no way to revive the snake.
	 * Once the snake is dead, it will no longer remain in the arena.
	 */
	final protected void die() {
		Console.addText("Oh, No!!!  Your snake died!");
		dead = true;
	}
	/**
	 * This method returns the state of the snake.
	 * @return true if dead
	 */
	final public boolean isDead() {
		return dead;
	}
	/**
	 * 
	 * @return true if the snake is active in the current arena
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * 
	 * @param active - sets the activity of the snake
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @return the head of the snake.  
	 * The location is cloned so that users cannot manually
	 * change the location of the snake's head
	 */
	final public LocI getHead() {
		return segs.get(0).clone();
	}
	/**
	 * @return the tail of the snake.  
	 * The location is cloned so that users cannot manually
	 * change the location of the snake's tail
	 */
	final public LocI getTail() {
		return segs.get(segs.size()-1).clone();
	}
	/**
	 * This method, when called, will get the size of the snake
	 * @return the size of the snake  
	 */
	final public int size() {
		return segs.size();
	}
	/**
	 * @return the ID of the snake
	 */
	final public int getId(){
		return id.get();
	}
	/**
	 * Sets the ID of the snake
	 * @param newID the new ID of the snake
	 */
	final public void setId(int newID){
		id.set(newID);
	}
	/**
	 * @return the name of the snake
	 */
	final public String getName(){
		return name.get();
	}
	/**
	 * This method allows users to get the locations of the segments of their snake. 
	 * @return the locations of the segments of the snake.
	 * The locations are cloned so that users cannot manually
	 * change the location of the snake's head.
	 */
	final public LocI[] getLocations() {
		LocI [] locations = new LocI[segs.size()];
		for (int i = 0; i < locations.length; i++) {
			locations[i] = segs.get(i).clone();
		}
		return locations;
	}
}