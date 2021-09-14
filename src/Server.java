
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Runnable{

    public static ArrayList<Socket> clientList = new ArrayList<>();
    //history function
    private static ArrayList<String> messages=new ArrayList<String>();

    public static void main(String args[]) {
        int port = 2000;
        try {
            ServerSocket serSocket = new ServerSocket(port);
            System.out.println("Server lauscht auf Port: " + port);
            while(true){
                Socket clientSocket = serSocket.accept();
                clientList.add(clientSocket);
                Thread t = new Thread(new Server(clientSocket)); //Für jeden neuen Client wird ein Thread gestartet
                t.start();
            }
        } catch (IOException ex) {
            System.out.println("Socket konnte nicht gehorcht werden.");
            ex.printStackTrace();
        }

    }

    private Socket clientSocket;
    //private ArrayList<Socket> clientList = null;

    private Server(Socket clientSocket){

        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {

        try {
            InputStreamReader socketStream = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(socketStream);
            System.out.println("TEST");

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

            writer.println("Sie sind Client Nummer: " + clientList.size());

            //history
            //Schicke den Verlauf der Unterhaltung an den neun Client
            for (String msg : messages) {
                writer.println(msg);
            }

            writer.flush();

            String line;
            while((line = in.readLine()) != null){
                System.out.println("TEST2");

                System.out.println("gelsen vom Client= "+line);

                messages.add(line);


                for(int i = 0; i < clientList.size(); i++){
                    PrintWriter writer2 = new PrintWriter(clientList.get(i).getOutputStream());
                    writer2.println(line);
                    writer2.flush();

                    System.out.println(clientList.size());
                }

            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
