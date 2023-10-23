import java.io.Serializable;

public class CFourInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    int playernumberREAL;
    boolean gameFinished = false;

    int playerTurn = 1;
    boolean bothConnected = false;

    private int row = -1;
    private int col = -1;

    CFourInfo() {}
    CFourInfo(int turn, int row, int col) {
        this.playerTurn = turn;
        this.row = row;
        this.col = col;
        this.bothConnected = true;
    }

    int getRow() { return row; }
    int getCol() { return col; }

}
