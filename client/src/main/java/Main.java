import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.*; 
import java.util.ArrayList;
import javafx.scene.Node;

public class Main extends Application {

	// gui components
	Scene introScene, gameScene, thirdScene;
	BorderPane introPane, gamePane, thirdPane;
	TextField portNum, ipAddress;
	Button connect;
	ListView<String> gameLog;
	public static GridPane gameBoard = new GridPane();
	Label winnerLabel;

	// backend variables
	Client clientConnection;
	boolean firstmessage = true;
	int playernum;
	ArrayList<ArrayList<GameButton>> gameBoardArray;

	// only activate buttons on lowest row
	void enableBoard() {
		// looping through gameboardarray
		for (int i = 0; i < 7; i++) {
			for (int j = 5; j >= 0; j--) {
				// String index = j + " " + i;
				// System.out.println(index);
				ArrayList<GameButton> row = gameBoardArray.get(j); // row
				GameButton button = row.get(i); // column
				
				if (button.owner == -1) {
					for (Node node : gameBoard.getChildren()) {
						if ((GridPane.getRowIndex(node) == j) &&
						    (GridPane.getColumnIndex(node) == i)) {
						    node.setDisable(false);
						}
					}
					break;
				}

			}
		}
	}

	void disableBoard() {
		for (Node each : gameBoard.getChildren()) {
			each.setDisable(true);
		}
	}

	int haveWinner() {
		// 4 in row
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				int cur = gameBoardArray.get(i).get(j).owner;

				if (gameBoardArray.get(i).get(j).owner == cur && 
					gameBoardArray.get(i).get(j+1).owner == cur &&
					gameBoardArray.get(i).get(j+2).owner == cur &&
					gameBoardArray.get(i).get(j+3).owner == cur &&
					cur != -1) {
					return cur;
				}
			}
		}
		// 4 in cols
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				int cur = gameBoardArray.get(i).get(j).owner;

				if (gameBoardArray.get(i).get(j).owner == cur && 
					gameBoardArray.get(i+1).get(j).owner == cur &&
					gameBoardArray.get(i+2).get(j).owner == cur &&
					gameBoardArray.get(i+3).get(j).owner == cur &&
					cur != -1) {
					return cur;
				}
			}
		}
		// first diagonals
		for (int i = 3; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				int cur = gameBoardArray.get(i).get(j).owner;

				if (gameBoardArray.get(i).get(j).owner == cur && 
					gameBoardArray.get(i-1).get(j+1).owner == cur &&
					gameBoardArray.get(i-2).get(j+2).owner == cur &&
					gameBoardArray.get(i-3).get(j+3).owner == cur &&
					cur != -1) {
					return cur;
				}
			}
		}
		// second diagonals
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				int cur = gameBoardArray.get(i).get(j).owner;

				if (gameBoardArray.get(i).get(j).owner == cur && 
					gameBoardArray.get(i+1).get(j+1).owner == cur &&
					gameBoardArray.get(i+2).get(j+2).owner == cur &&
					gameBoardArray.get(i+3).get(j+3).owner == cur &&
					cur != -1) {
					return cur;
				}
			}
		}

		int count = 0;
		for (Node node : gameBoard.getChildren()) {
			GameButton gb = (GameButton) node;
			if (gb.owner == -1) {
				count++;
			}
		}
		if (count == 42) {
			return 3;
		}

		return -1;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("CLIENT");

		introPane = new BorderPane();
		gamePane = new BorderPane();
		thirdPane = new BorderPane();
		introPane.setStyle("-fx-font-family: SansSerif;-fx-background-color: lightblue;");
		gamePane.setStyle("-fx-font-family: SansSerif;-fx-background-color: lightblue;");
		thirdPane.setStyle("-fx-font-family: SansSerif;-fx-background-color: lightblue;");

		introScene = new Scene(introPane, 600, 400);
		gameScene = new Scene(gamePane, 600, 400);
		thirdScene = new Scene(thirdPane, 600, 400);

		// create intro scene
		portNum = new TextField();
		portNum.setPromptText("Enter port");
		ipAddress = new TextField();
		ipAddress.setPromptText("Enter ip address"); // assumer user enters a valid one
		connect = new Button("Connect");
		VBox introBox = new VBox(portNum,ipAddress,connect);
		introBox.setPrefWidth(100);

		introPane.setCenter(introBox);
		introBox.setAlignment(Pos.CENTER);

		primaryStage.setScene(introScene);
		primaryStage.show();

		connect.setOnAction(b -> {
			clientConnection = new Client(ipAddress.getText(), Integer.parseInt(portNum.getText()),
				data -> {
					Platform.runLater( () -> {
						if (firstmessage) {
							CFourInfo temp = (CFourInfo) data;
							playernum = temp.playernumberREAL;
							firstmessage = false;
						} else {
							enableBoard();
						}
					});
				},
				data2 -> {
					Platform.runLater( () -> {
						// data2 is a string of form "row + col + playernumber"
						// "501"
						String str = (String) data2;
						int r = Integer.parseInt(str.substring(0,1));
						int c = Integer.parseInt(str.substring(1,2));
						int n = Integer.parseInt(str.substring(2));

						// apply this to both of the board formats
						// GridPane
						for (Node node : gameBoard.getChildren()) {
							if ((GridPane.getRowIndex(node) == r) && 
							   (GridPane.getColumnIndex(node) == c)) {
								GameButton g = (GameButton) node;
								g.owner = n;

								// change it to red
								if (g.owner == 1) {
									g.setStyle("-fx-background-color: #ff0000");
								} 
								// change it to yellow
								else {
									g.setStyle("-fx-background-color: #ffff00");
								}
							}
						}

						// ArrayList
						gameBoardArray.get(r).get(c).owner = n;

						int w = haveWinner();
						if (w == 1) {
							winnerLabel = new Label("Player 1 wins!");
							primaryStage.setScene(thirdScene);
						} else if (w == 2) {
							winnerLabel = new Label("Player 2 wins!");
							primaryStage.setScene(thirdScene);
						} else if (w == 3) {
							winnerLabel = new Label("It was a tie!");
							primaryStage.setScene(thirdScene);

						}
					});
				}
			);

			clientConnection.start();
			primaryStage.setScene(gameScene);
		});

		// creates the private board for the client
		gameBoardArray = new ArrayList<ArrayList<GameButton>>(6);
		for (int i = 0; i < 6; i++) {
			ArrayList<GameButton> temp = new ArrayList<>(7);

			for (int j = 0; j < 7; j++) {
				temp.add(new GameButton(j,i));
			}

			gameBoardArray.add(temp);
		}

		// create game scene
		for (int i = 0; i < 6; i++) { // rows
			for (int j = 0; j < 7; j++) { // cols
				GameButton gb = new GameButton(j, i);
				gb.setPrefSize(50,50);
				gb.setDisable(true);

				// // testing purposes to print row and col number on button
				// String testing = i + " " + j;
				// gb.setText(testing);

				gameBoard.add(gb, j, i);

				gb.setOnAction(b -> {
					if (playernum == 1 && gb.owner == -1) {
						// set owner for gridpane
						gb.owner = 1;
						gb.setStyle("-fx-background-color: #ff0000"); // red
						disableBoard();
						// set owner for array
						int row = GridPane.getRowIndex(gb);
						int col = GridPane.getColumnIndex(gb);
						ArrayList<GameButton> selectedRow = gameBoardArray.get(row);
						GameButton selectedButton = selectedRow.get(col);
						selectedButton.owner = 1;

						// create and send cfourinfo object here
						CFourInfo obj = new CFourInfo(2, gb.row, gb.col); // here is where the turns get swapped
						clientConnection.send(obj);
					
					// PlayerNumber = 2
					} else if (playernum == 2 && gb.owner == -1) {
						// set owner for gridpane
						gb.owner = 2;
						gb.setStyle("-fx-background-color: #ffff00"); // yellow
						disableBoard();
						// set owner for array
						int row = GridPane.getRowIndex(gb);
						int col = GridPane.getColumnIndex(gb);
						ArrayList<GameButton> selectedRow = gameBoardArray.get(row);
						GameButton selectedButton = selectedRow.get(col);
						selectedButton.owner = 2;
						CFourInfo obj = new CFourInfo(1, gb.row, gb.col); // here is where the turns get swapped
						clientConnection.send(obj);
					}
				});
			}
		}

		// do not need bullet points 3, 4, or 5 inside of client gui for game because button 
		//     vailidity takes care of that
		gamePane.setCenter(gameBoard);
		gameBoard.setAlignment(Pos.CENTER);

		// create final scene
		Button playAgain = new Button("Play again");
		Button exit = new Button("Exit");

		playAgain.setOnAction(b -> {
			// reset both boards
			primaryStage.setScene(gameScene);
			gameBoardArray = new ArrayList<ArrayList<GameButton>>(6);
			for (int i = 0; i < 6; i++) {
				ArrayList<GameButton> temp = new ArrayList<>(7);
	
				for (int j = 0; j < 7; j++) {
					temp.add(new GameButton(j,i));
				}
	
				gameBoardArray.add(temp);
			}

			for (Node each : gameBoard.getChildren()) {
				each.setDisable(true);
				each.setStyle("-fx-background-color: #000000");
			}


		
		
		

		});
		exit.setOnAction(b -> {
			Platform.exit();
			System.exit(0);
		});
		winnerLabel = new Label();
		HBox buttons = new HBox(playAgain, exit);
		VBox finalScreen = new VBox(winnerLabel, buttons);

		thirdPane.setCenter(finalScreen);
		finalScreen.setAlignment(Pos.CENTER);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
