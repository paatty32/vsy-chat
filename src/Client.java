import java.io.*;
import java.net.Socket;

public class Client {

    public static void main (String [] args){
        try{
            Socket socket = new Socket("localhost", 2000);
            System.out.println("Verbindung war erfolgreich.");

            //Für die Daten die der Client vom Server erhält
            InputStreamReader socketStream = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(socketStream);


            //Damit wird zum server geschrieben.
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("Hallo Server");
            writer.flush();

            String line;
            while((line = in.readLine()) != null){
                System.out.println("Client: gelesen vom Server= " + line);


            }

            in.close();
        } catch (IOException ex){
            System.out.println("Konnte Socket nicht lesen/schreiben");
            ex.printStackTrace();
        }
    }

}
