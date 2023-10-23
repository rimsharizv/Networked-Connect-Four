import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {

    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback, callback2;
    private int port;
    public int countClients = 0;
    CFourInfo master = new CFourInfo();

    Server(int port, Consumer<Serializable> call, Consumer<Serializable> call2) {
        this.port = port;
        callback = call;
        callback2 = call2;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread {

        public void run() {
            try (ServerSocket mysocket = new ServerSocket(port);) {
                callback.accept("Server is waiting for a client!");

                while (true) {
                    if (countClients < 2) {
                        ClientThread c = new ClientThread(mysocket.accept(), (countClients+1));
                        callback.accept("client has connected to server: client #" + (countClients+1));
                        callback2.accept("1");
                        clients.add(c);

                        c.start();
                        countClients++;
                    }
                }
            }
            catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }
    }

    // responsible for sending and recieving information about the game between clients
    // thread created for each client connected to the server
    // *** create datamember in clientthread to denote which player this thread is serving
    class ClientThread extends Thread {

        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.count = count;
        }

        public void updateClients() {
            for (int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    if (clients.size() == 2) {
                        // send both players object telling them that game started
                        CFourInfo c = new CFourInfo();
                        c.playernumberREAL = (i+1);
                        c.bothConnected = true;
                        t.out.writeObject(c);
                    } else {
                        System.out.println("not enougth players yet");
                    }
                } catch (Exception e) {}
            }
        }

        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }
            
            updateClients(); // incrase the number of player sthat are connected by 1

            // while game is still running
            while (!master.gameFinished) {
                try {
                    CFourInfo data = (CFourInfo) in.readObject();
                
                    try {
                        // send out to all the clients 
                        ClientThread c1 = clients.get(0);
                        ClientThread c2 = clients.get(1);
                        
                        c1.out.writeObject(data);
                        c2.out.writeObject(data);


                    } catch (Exception e) {}


                } catch (Exception e) {
                    callback.accept("Client #" + count + " left");
                    callback2.accept("-1");
                    clients.remove(this);
                    countClients--;
                    break;
                }
            }


        }

                    
    }


	
}
