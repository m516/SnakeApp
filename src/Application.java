import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application extends JPanel implements ActionListener, WindowListener{
	private static JFrame frame = new JFrame();
	JPanel secondaryPanel = new JPanel();
	private Button loadButton = new Button("Press me!");
	private ServerBridge socket;
	private Map<Class<?>,String> snakeTypes = new HashMap<Class<?>, String>();
	private static final long serialVersionUID = -2517616123689799182L;
	public Application(){
	}
	private void init(){
		//Initialize the socket
		socket = new ServerBridge("127.0.0.1", 2060);
		frame.setSize(640, 480);
		//Set the general layout of the window
		setLayout(new FlowLayout());
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//Initialize and add the arena to the window
		Arena.init(8,8,2);
		add(Arena.instance);
		//Create a secondary panel that contains the console, a button and whatever else it needs
		secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));
		//Configure a button and add it to the secondary panel
		loadButton.addActionListener(this);
		secondaryPanel.add(loadButton);
		//Add the console to the secondary panel
		Console.init();
		secondaryPanel.add(Console.gui);
		//Add the secondary panel to the window
		add(secondaryPanel);
		//Add all of the contents into a JFrame
		frame.add(this);
		//Configure the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(this);
		//Now display it.
		frame.setVisible(true);
		//Run the socket
		Console.addText("Connecting to server...");
		Console.addText("Socket live before connecting: " + socket.isConnected());
		socket.connectToServer();
		Console.addText("Socket live after connecting: " + socket.isConnected());
		//Did the socket connect?
		if(socket.isConnected())  Console.addText("Connected!");
		else Console.addText("Failed to Connect!");
		Console.addText("Socket live before listening: " + socket.isConnected());
		//Initialize and configure the snake.
		AppConfig.addSnakes();
		socket.listenAndParse();
		Console.addText("Socket live after listening: " + socket.isConnected());
	}
	public static void main(String[] args) {
		Application a = new Application();
		a.init();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {
			Console.addText("Button pressed!");
		}
	}
	public static void validateFrame(){
		frame.validate();
	}
	public static void invalidateFrame(){
		frame.invalidate();
	}
	@Override
	public void windowActivated(WindowEvent e) {
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowClosing(WindowEvent e) {
		socket.closeSocket();
		socket = null;
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowOpened(WindowEvent e) {
	}
	public void addSnakeType(Class<?> snakeClass, String snakeName) {
		snakeTypes.put(snakeClass, snakeName);
	}
	public static class Console{
		private static String[] lines = new String[64];
		private static int numLines = 0;
		private static TextArea gui = new TextArea("Welcome!");
		public static void init() {
			gui.setEditable(false);
			lines = new String[64];
			numLines = 0;
		}
		public static void addText(String str) {
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
		}
	}
}
