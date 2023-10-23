import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

// responsible for send and recieveing data and calculating if a move results in a win
public class Client  extends Thread {
    
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    private String ip;
    private int port;

    private Consumer<Serializable> callback, callback2;

    boolean firstmessage = true;
    int playernum = -1;

    Client(String ip, int port, Consumer<Serializable> call, Consumer<Serializable> call2) {
        this.ip = ip;
        this.port = port;
        this.callback = call;
        this.callback2 = call2;
    }

    public void run() {
        try {
            socketClient = new Socket(ip, port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);

        } catch (Exception e) {}

        while (true) {
            try {
                // recieve object from out.writeObject() from server
                CFourInfo message = (CFourInfo) in.readObject();

                if (firstmessage) {
                    playernum = message.playernumberREAL;
                    firstmessage = false;
                    callback.accept(message);
                } else {
                    // here is where I will callback accept to update board
                    int op_row = message.getRow();
                    int op_col = message.getCol();
                    int op_num = -1;
                    if (message.playerTurn == 1) { op_num = 2; }
                    else                         { op_num = 1; }

                    String oponentsMove = "" +  op_row + op_col + op_num;

                    callback2.accept(oponentsMove);
                }

                // System.out.println("==========");
                // System.out.println("playernum       : " + playernum);
                // System.out.println("first condition : " + (message.bothConnected));
                // System.out.println("second conditoin: " + message.playerTurn + " == " + playernum);

                // if it is clients turn then 
                if (message.bothConnected && (message.playerTurn == playernum)) {
                    callback.accept(message);
                }
                

            } catch (Exception e) {}
        }
    }

    public void send(CFourInfo data) {
        try {
            out.writeObject(data);
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}