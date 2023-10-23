import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.*;
import javafx.application.Platform;
import javafx.scene.control.TextField;


public class Main extends Application {
	
	Scene startScene, logScene;
	BorderPane startPane, logPane;
	ListView serverLog;
	private int port;
	Server serverConnection;
	Label numClientsNum;
	int p1,p2;
	Label player1score;
	Label player2score;

	public boolean isNum(String num) {
        if (num == "") { return false; }

        try { int i = Integer.parseInt(num); } 
        catch (NumberFormatException e) { return false; }
        return true;
    }

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Server");
		startPane = new BorderPane();
		logPane = new BorderPane();
		startPane.setStyle("-fx-font-family: SansSerif;-fx-background-color: lightgreen;");
		logPane.setStyle("-fx-font-family: SansSerif;-fx-background-color: lightgreen;");
		startScene = new Scene(startPane, 600, 400);
		logScene = new Scene(logPane, 600, 400);

		// set up start pane
		Label portLabel = new Label("Enter a port: ");
		TextField portTF = new TextField();
		Button portSend = new Button("Send");
		HBox starthbox = new HBox(portLabel, portTF, portSend);
		startPane.setCenter(starthbox);
		starthbox.setAlignment(Pos.CENTER);

		// set up log pane
		Label serverStatusLabel = new Label("Server status: ");
		Label serverStatusStatus = new Label("Online");
		HBox statushbox = new HBox(serverStatusLabel, serverStatusStatus);
		statushbox.setAlignment(Pos.CENTER);

		Label numClientsLabel = new Label("Number of clients: ");
		numClientsNum = new Label("0");
		HBox clientshbox = new HBox(numClientsLabel, numClientsNum);
		clientshbox.setAlignment(Pos.CENTER);

		VBox topInfo = new VBox(statushbox, clientshbox);

		Label logLabel = new Label("Server Log");
		serverLog = new ListView();
		VBox logvbox = new VBox(logLabel, serverLog);
		logvbox.setPadding(new Insets(10,10,10,10));

		VBox overall = new VBox(topInfo, logvbox);
		overall.setPadding(new Insets(30,30,30,30));

		logPane.setCenter(overall);

		primaryStage.setScene(startScene);
        primaryStage.show();

		portSend.setOnAction(b -> {
			if (portTF.getText() != "" && isNum(portTF.getText())) {
				port = Integer.parseInt(portTF.getText());
				primaryStage.setScene(logScene);

				serverConnection = new Server(
					port,
					data -> {
						Platform.runLater( () -> {
							serverLog.getItems().add(data.toString());
						});
					},
					data2 -> {
						Platform.runLater( () -> {
							String asString = (String) data2;
							int data2int = Integer.parseInt(asString);
							int newNum = Integer.parseInt(numClientsNum.getText()) + data2int;
							numClientsNum.setText(String.valueOf(newNum));
						});
					}
				);
			}
		});






	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
