package application;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Arena{
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
	private static GraphicsContext graphics;
	static Color bkg;
	private static Canvas canvas;
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
	public static void init(int new_x_size, int new_y_size){
		arena = new byte[new_x_size][new_y_size];
		xSize = new_x_size;
		ySize = new_y_size;
		for (int i = 0; i < arena.length; i++) {
			for (int j = 0; j < arena[i].length; j++) {
				arena[i][j] = ERR;
			}
		}
	}
	public static void setBlock(int x, int y, byte type){
		arena[x][y] = type;
	}
	public static int getBlock(int x, int y){
		return arena[x][y];
	}
	public static void setCanvas(Canvas newCanvas){
		canvas = newCanvas;
		graphics = canvas.getGraphicsContext2D();
	}

	public synchronized void repaint(){
		Platform.runLater(() -> {
			int snakeID = SnakeManager.getSnake(0).getID()-1 - FRUIT;
			if(snakeID > 0){
				bkg = getSnakeColor(snakeID).darker().darker();
			}
			else{
				bkg = Color.BLACK;
			}
			graphics.setFill(bkg);
			graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
			for(int i = 0; i < arena.length; i ++){
				byte[] column = arena[i];
				Paint c;
				for (int j = 0; j < column.length; j++) {
					byte cell = arena[i][j];
					switch(cell){
					case EMPTY:
						if(bkg==null)
							c = Color.BLACK;
						else c = bkg;
						break;
					case WALL:
						c  = Color.LIGHTGRAY;
						break;
					case FRUIT: 
						c = Color.MAGENTA;
						break;
					default:
						c = getSnakeColor(cell-1-FRUIT);
						break;
					}
					drawCell(i,j,c);
				}

			}
		});
	}

	void drawCell(int x, int y, Paint c){
		int blockWidth = (int) (canvas.getWidth()/arena.length);
		int blockHeight = (int) (canvas.getHeight()/arena[0].length);
		//Rectangle rect = new Rectangle(x*blockWidth, y*blockHeight, blockWidth, blockHeight);
		graphics.setFill(Color.web(c.toString()));
		graphics.fillRect(x*blockWidth, y*blockHeight, blockWidth, blockHeight);
	}
	/**
	 * Returns a constructed rectangle at a certain position in the arena. 
	 * This method is depreciated because all of the cells are drawn directly on the Arena canvas
	 * @param x - the x-coordinate of the cell in the arena
	 * @param y - the y-coordinate of the cell in the arena
	 * @param p - the color/gradient/texture of the cell
	 * @return a rectangle to be displayed on the Arena canvas
	 */
	@Deprecated
	Rectangle getCell(int x, int y, Paint p){
		int blockWidth = (int) (canvas.getWidth()/arena.length);
		int blockHeight = (int) (canvas.getHeight()/arena[0].length);
		Rectangle r = new Rectangle(x*blockWidth, y*blockHeight, blockWidth, blockHeight);
		r.setFill(p);
		return r;
	}
	public synchronized static boolean retrieveCommand(int commandType, Integer[] command){
		try{
			switch(commandType){
			case ARENA_CONFIG:
				Console.addText("Arena configured");
				init(command[0], command[1]);
				break;
			case ARENA_DISPLAY:
				for(int i = 0; i < command.length; i ++){
					int num = command[i];
					int y = i%ySize, x = i/ySize;
					setBlock(x,y,(byte)num);
				}
				instance.repaint();
				break;
			default:
				return false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Color getSnakeColor(int snakeNumber){
		switch(snakeNumber){
		case 0: return Color.BLUE; 
		case 1: return Color.RED; 
		case 2: return Color.GREEN; 
		case 3: return Color.YELLOW; 
		case 4: return Color.ORANGE; 
		case 5: return Color.PURPLE; 
		case 6: return Color.DODGERBLUE; 
		case 7: return Color.INDIGO; 
		case 8: return Color.AQUA; 
		case 9: return Color.DARKRED; 
		case 10: return Color.DARKBLUE; 
		case 11: return Color.DARKGREEN; 
		case 12: return Color.DARKORANGE; 
		case 13: return Color.DARKORCHID; 
		case 14: return Color.GOLD; 
		case 15: return Color.PERU; 
		case 16: return Color.TEAL; 
		case 17: return Color.YELLOWGREEN; 
		default:
			return Color.hsb(((double)snakeNumber)*11%1, 0.5, 1.0);
			
		
		}
	}
}

