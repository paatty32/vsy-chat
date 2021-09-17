package backend;

import java.io.*;
import java.net.Socket;

public class Client {

    private InputStreamReader socketStream;
    private BufferedReader in;
    private PrintWriter writer;
    private Socket socket;

    private String host;
    private int port;

    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect(int port){
        try{
            this.socket = new Socket("localhost", port);

            System.out.println("Verbindung war erfolgreich.");

            //Für die Daten die der Client vom Server erhält
            this.socketStream = new InputStreamReader(socket.getInputStream());
            this.in = new BufferedReader(socketStream);

             writer = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
            //verbinde dich mit dem backupserver
            try {
                this.socket = new Socket("localhost", 2001);

                System.out.println("Verbindung war erfolgreich.");

                //Für die Daten die der Client vom Server erhält
                this.socketStream = new InputStreamReader(socket.getInputStream());
                this.in = new BufferedReader(socketStream);

                writer = new PrintWriter(socket.getOutputStream());

            } catch (IOException ioException) {
                ioException.printStackTrace();
              }
        }
    }


    public void writetoServer(String text){
        writer.println(text);
        writer.flush();
    }

    public String recieveFromServer() throws IOException {
        String line;
        while((line = this.in.readLine()) != null){

            return line;
        }
        //Verbinde dich mit dem Backup-Server, wenn keine Nachrichten mehr erhalten werden können.
        if(this.in.readLine() == null){
            this.connect(2001);
        }
        return "";
    }
}
