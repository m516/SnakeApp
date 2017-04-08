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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class GUIController implements Initializable {

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Button buttonServer;

    @FXML
    private TableView<Snake> listSnakes;

    @FXML
    private TableColumn<Snake, String> columnSnakeNames;

    @FXML
    private TableColumn<Snake, Integer> columnID;

    @FXML
    private TextField textfieldServer;
    
    @FXML
    void buttonGoPressed(ActionEvent event) {
    	System.out.println("Go button pressed");
    	AppManager.getCurrentSnakeManager().closeAllBridges();
		//Initialize and configure the snake.
		AppConfig.addSnakes();
		System.out.println("AppManager Initialized!");
		AppManager.getCurrentSnakeManager().connectSnakesToServer(textfieldServer.getText(), 6419);
		listSnakes.getSelectionModel().select(0);
		tableEdited(null);
    }

    @FXML
    void serverAddressEntered(ActionEvent event) {
    	
    }
    

	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		System.out.println("GUIController initialized");
		//Configure the app manager
		textName.setText(AppManager.getCurrentSnakeManager().getSnake(0).getName());
		Arena.setCanvas(arena);
		//Initialize the table
		listSnakes.setItems(AppManager.getCurrentSnakeManager().getSnakes());
		//Initialize the column
		columnSnakeNames.setCellValueFactory(new PropertyValueFactory<Snake,String>("name"));
		columnID.setCellValueFactory(new PropertyValueFactory<Snake,Integer>("id"));
		
		
	}
	
	
    @FXML
    void tableEdited(MouseEvent event) {
    	Snake currentSnake = listSnakes.getSelectionModel().getSelectedItem();
    	if(currentSnake != null){
    		Arena.setBkg(Arena.getSnakeColor(currentSnake.getId()).darker().darker());
    		textName.setText(currentSnake.getName() + " (" + currentSnake.getId() + ")");
    	}
    	listSnakes.refresh();
    }

}
