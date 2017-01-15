import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class MainServer {
	public static final int END = -1, ARENA_CONFIG = -2, ARENA_DISPLAY = -3;
	private static ArrayList<ClientBridge> bridges = new ArrayList<ClientBridge>();
	private static ClientBridge initialConnectionPoint = new ClientBridge();
	public static void main(String[] args) throws IOException {


		int portNumber =2060;

		try {
			for(int i = 0; i < 2; i ++){
				initialConnectionPoint = new ClientBridge(portNumber);
				if(!initialConnectionPoint.init()) System.exit(0);
				ClientBridge b = new ClientBridge();
				bridges.add(b);
				initialConnectionPoint.getOutStream().println(b.getPort());
				String inputLine = initialConnectionPoint.getInStream().readLine();
				System.out.println("Client: " + inputLine);
				initialConnectionPoint.closeConnection();
				if(!b.init()) System.exit(0);
				PrintWriter out = b.getOutStream();
				BufferedReader in = b.getInStream();
				while((inputLine = in.readLine()) != null){
					System.out.println(inputLine);
					if(inputLine.equals("Requesting test response")){
						out.println("Success");
						System.out.println("Success!");
						break;
					}
				}
			}
			Thread.sleep(3000);
			spam(ARENA_CONFIG);
			spam(4);
			spam(4);
			spam(8);
			spam(END);
			for(int i = 0; i < 1; i ++){
				spam(ARENA_DISPLAY);
				for(int j = 0; j < 4; j ++){
					for(int k = 0; k < 4; k ++){
						spam((j+k+i)%8+4);
					}
				}
				spam(END);
			}
			close();
			System.out.println("Done!");
		} catch (Exception e) {
			System.out.println("Exception caught when trying to listen on port "
					+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
	private static void spam(int msg){
		System.out.println(msg);
		for(ClientBridge bridge:bridges){
			bridge.getOutStream().write(msg);
		}
	}
	private static void close(){
		for(ClientBridge bridge:bridges){
			bridge.closeConnection();
		}
	}

	private static class ClientBridge{
		private ServerSocket connectionSocket;
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		public ClientBridge(){
			try {
				connectionSocket = new ServerSocket(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public ClientBridge(int port){
			try {
				connectionSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public boolean init(){
			try {
				socket = connectionSocket.accept();
				out = new PrintWriter(socket.getOutputStream(),true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		public void closeConnection(){
			try {
				connectionSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public int getPort(){
			return connectionSocket.getLocalPort();
		}
		public PrintWriter getOutStream() {
			return out;
		}
		public BufferedReader getInStream() {
			return in;
		}
	}
}