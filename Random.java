import java.io.Serializable;

class MorraInfo implements Serializable {

    int playernumber = 0; // one time var
    int playernumberREAL;
    private static final long serialVersionUID =1L;

    MorraInfo(int play, int guess, int PlayerNumber) {
        if (PlayerNumber == 1) {
            p1Plays = play;
            p1Guess = guess;
            player1played = true;
        } else {
            p2Plays = play;
            p2Guess = guess;
            player2played = true;
        }
        playernumberREAL = PlayerNumber;
    }

    MorraInfo() {}

    // if have2players is true -> evaluate it to see who won
    boolean have2players;
    // max 2 to win
    int p1Points = 0;
    int p2Points = 0;
    // what each player played
    int p1Plays;
    int p2Plays;
    // what each player guessed
    int p1Guess;
    int p2Guess;

    boolean player1played = false;
    boolean player2played = false;
    boolean endOfRound = false;

    int getp1play() { return p1Plays; }
    int getp1guess() { return p1Guess; }
    int getp2play() { return p2Plays; }
    int getp2guess() { return p2Guess; }

    int tempnum = 0;
}
