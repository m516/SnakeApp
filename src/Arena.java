import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Arena extends JPanel {
	private static final long serialVersionUID = 1L;
	/*
	 * Arenas are composed of byte arrays which represent what occupies each cell in the array
	 * ERR   - Dark grey      - no data retrieved or bad data given for this cell
	 * EMPTY - Black          - this cell is unoccupied
	 * WALL  - White          - this cell is a wall block
	 * FRUIT - Magenta        - this cell is occupied by a fruit
	 * SNAKE - snake-specific - this cell is occupied by a snake segment
	 */
	public static final byte ERR = 0, EMPTY = 1, WALL = 2, FRUIT = 3;
	/*
	 * Commands from the server to the arena are sent with these keys
	 * ERR             - bad message to server, followed by a command number, which 
	 * 					requests the client application to repeat the command
	 * ARENA_CONFIG    - requests the client to resize the arena to the x_size and y_size
	 * ARENA_DISPLAY   - the updated arena, followed by all of the pixels
	 * END             - An end of a command
	 */
	public static final int END = -2, ARENA_CONFIG = -3, ARENA_DISPLAY = -4, SNAKE_CONFIG = -6;
	private static volatile byte[][] arena;
	private static int xSize, ySize;
	public static Arena instance = new Arena();
	private static Color[] snakeColors;
	private static Graphics2D graphics;
	private static SnakeRoot mySnake;
	private static Color bkg;
	private Arena(){
	}
	public static int getXSize() {
		return xSize;
	}
	public static int getYSize() {
		return ySize;
	}
	/**Initializes a new arena
	 * Snakes must not call this method or ANY OTHER MUTATOR METHODS in this class!  
	 * It will throw errors in the application and will not change anything in the server.
	 */
	public static void init(int new_x_size, int new_y_size, int numSnakes){
		arena = new byte[new_x_size][new_y_size];
		xSize = new_x_size;
		ySize = new_y_size;
		for (int i = 0; i < arena.length; i++) {
			for (int j = 0; j < arena[i].length; j++) {
				arena[i][j] = ERR;
			}
		}
		snakeColors = new Color[numSnakes+1];
		for(int i = 0; i <= numSnakes; i ++){
			snakeColors[i] = Color.getHSBColor((float)i/numSnakes, 1f, 1f);
		}
		Dimension d = new Dimension(xSize*8, ySize*8);
		Application.invalidateFrame();
		instance.invalidate();
		instance.setPreferredSize(d);
		instance.setSize(d);
		instance.validate();
		Application.validateFrame();
	}
	public static void setBlock(int x, int y, byte type){
		arena[x][y] = type;
	}
	public static int getBlock(int x, int y){
		return arena[x][y];
	}
	public static void addSnake(SnakeRoot s){
		mySnake = s;
	}
	public static void killSnake(){
		mySnake.die();
	}
	public static String move(){
		return "" + mySnake.update();
	}

	@Override
	public void paint(Graphics g){
		graphics = (Graphics2D) g;
		for(int i = 0; i < arena.length; i ++){
			byte[] column = arena[i];
			Color c;
			for (int j = 0; j < column.length; j++) {
				byte cell = arena[i][j];
				switch(cell){
				case EMPTY:
					if(bkg==null)
					c = Color.BLACK;
					else c = bkg;
					break;
				case WALL:
					c  = Color.LIGHT_GRAY;
					break;
				case FRUIT: 
					c = Color.MAGENTA;
					break;
				default:
					if(cell < snakeColors.length+FRUIT && cell > FRUIT){
						c = snakeColors[cell-FRUIT-1];
					}
					else c = Color.DARK_GRAY;
					break;
				}
				graphics.setPaint(c);
				drawCell(graphics, i, j, c);
			}
		}
	}

	void drawCell(Graphics2D g, int x, int y, Color c){
		int blockWidth = getWidth()/arena.length;
		int blockHeight = getHeight()/arena[0].length;
		g.setColor(c);
		g.fillRect(x*blockWidth, y*blockHeight, blockWidth, blockHeight);
	}
	public static boolean retrieveCommand(int commandType, Integer[] command){
		try{
			switch(commandType){
			case ARENA_CONFIG:
				init(command[0], command[1], command[2]);
				break;
			case ARENA_DISPLAY:
				for(int i = 0; i < command.length; i ++){
					int num = command[i];
					int y = i%ySize, x = i/ySize;
					setBlock(x,y,(byte)num);
				}
				break;
			case SNAKE_CONFIG:
				//Get the ID of the snake
				mySnake.setID(command[0]);
				Application.Console.addText("ID: "+mySnake.getID());
				Application.Console.addText("Size: "+(command.length-1)/2);
				LocI[] locations = new LocI[(command.length-1)/2];
				//TODO this isn't completing the loop
				for(int i = 1; i < command.length-1; i += 2){
					locations[(i-1)/2] = new LocI(command[i], command[i+1]);
				}
				mySnake.init(locations);
				Application.Console.addText("********Snake initialized");
				for(LocI l: locations){
					Application.Console.addText(l.toString());
				}
				bkg = snakeColors[mySnake.getID()-1-FRUIT].darker().darker();
				break;
			default:
				return false;
			}
		}
		catch(Exception e){return false;}
		instance.repaint();
		return true;
	}
}

