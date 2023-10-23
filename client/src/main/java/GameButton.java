import javafx.scene.control.*;

public class GameButton extends Button {

    int row;
    int col;
    int owner = -1;

    GameButton(int col, int row) {
        this.row = row;
        this.col = col;
    }

}
