package gui;
import java.net.URL;
import java.util.ResourceBundle;
import application.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class GUIController implements Initializable {

    @FXML
    private AnchorPane windowPane;

    @FXML
    private AnchorPane arenaPane;

    @FXML
    private Canvas arena;

    @FXML
    private Canvas arenaInfo;

    @FXML
    private AnchorPane infoPane;

    @FXML
    private Label textName;

    @FXML
    private LineChart<?, ?> statusGraph;

    @FXML
    private Label textWins;

    @FXML
    private Label textLosses;

    @FXML
    private Label textStatus;

    @FXML
    private TextField textfieldServer;

    @FXML
    private Button buttonServer;

    @FXML
    private ProgressBar progressServer;

    @FXML
    private TableView<?> listSnakes;

    @FXML
    private TableColumn<?, ?> mySnakes;
    
    AppManager appManager;
    
    @FXML
    void buttonGoPressed(ActionEvent event) {
    	System.out.println("Button pressed");
		appManager.connectToServer();
    }

    @FXML
    void serverAddressEntered(ActionEvent event) {
    	
    }
    

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		System.out.println("GUIController initialized");
		appManager = AppManager.getCurrentAppManager();
		textName.setText(Arena.getSnake().getName());
		Arena.setCanvas(arena);
		System.out.println("Controller AppManager instance: " + appManager);
		
		
	}
	
	public void setAppManagerInstance(AppManager am){
		appManager = am;
		System.out.println("Controller AppManager instance: " + appManager);
	}

}
