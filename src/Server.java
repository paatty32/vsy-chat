import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class Server implements Runnable{

    public static void main(String args[]) {
        int port = 2000;
        try {
            ServerSocket serSocket = new ServerSocket(port);
            System.out.println("Server lauscht auf Port: " + port);
            while(true){
                Socket clientSocket = serSocket.accept();
                Thread t = new Thread(new Server(clientSocket));
                t.start();
            }
        } catch (IOException ex) {
            System.out.println("Socket konnte nicht gehorcht werden.");
            ex.printStackTrace();
        }

    }
    private Socket clientSocket;
    private Server(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            InputStreamReader socketStream = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(socketStream);
            System.out.println("TEST");
            //PrintStream stream = System.out;
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

            String line;
            while((line = in.readLine()) != null){
                System.out.println("TEST2");

                System.out.println("Server: gelsen vom Client= "+line);
                writer.println(line);
                writer.flush();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
