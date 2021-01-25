import java.io.*;
import java.net.Socket;
import java.util.Timer;

public class Client {

    public static void main (String [] args){
        try{
            Socket socket = new Socket("localhost", 2000);

            System.out.println("Verbindung war erfolgreich.");

            //Für die Daten die der Client vom Server erhält
            InputStreamReader socketStream = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(socketStream);

            //Tastatureingabe
            InputStreamReader tastatur = new InputStreamReader(System.in);
            BufferedReader tastaturIn = new BufferedReader(tastatur);

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            /*
            Heartbeat heartbeat = new Heartbeat(writer);
            Timer timer = new Timer();
            timer.schedule(heartbeat, 0, 2000);
*/
            String line;

            //Innere Thread Klasse erstellen, fürs Schreiben
            class WriterThread extends Thread{
                @Override
                public void run() {
                    while(true){
                        try {
                            if(writer.checkError()){
                                System.out.println("Verbindung zum Server Unterbrochen, drücke ENTER zu erneuten Aufbau");
                                break;
                            } else {
                                System.out.print(">>: ");
                                writer.println(tastaturIn.readLine());
                                writer.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }

            WriterThread writerThread = new WriterThread();
            writerThread.start();

            while(true){
                //Schreiben zum Server
                //Lesen vom Server -> es wird nur gelesen, wenn auch etwas drin ist
                while(((line = in.readLine())!= null)){
                    System.out.println("Client: gelesen vom Server= " + line);
                }
                in.close();
            }

        } catch (IOException ex){
            System.out.println("Verbindung fehlgeschlagen");

            ex.printStackTrace();

            try{
                Socket socket = new Socket("localhost", 2001);

                System.out.println("Verbindung war erfolgreich.");

                //Für die Daten die der Client vom Server erhält
                InputStreamReader socketStream = new InputStreamReader(socket.getInputStream());
                BufferedReader in = new BufferedReader(socketStream);

                //Tastatureingabe
                InputStreamReader tastatur = new InputStreamReader(System.in);
                BufferedReader tastaturIn = new BufferedReader(tastatur);

                PrintWriter writer = new PrintWriter(socket.getOutputStream());

            /*
            Heartbeat heartbeat = new Heartbeat(writer);
            Timer timer = new Timer();
            timer.schedule(heartbeat, 0, 2000);
            */

                //Innere Thread Klasse erstellen, fürs Schreiben
                class WriterThread extends Thread{
                    @Override
                    public void run() {
                        while(true){

                            try {
                                writer.println(tastaturIn.readLine());
                                writer.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                WriterThread writerThread = new WriterThread();
                writerThread.start();

                String line;

                while(true){
                    //Schreiben zum Server
                    //Lesen vom Server -> es wird nur gelesen, wenn auch etwas drin ist
                    while(((line = in.readLine())!= null)){
                        System.out.println("Client: gelesen vom Server= " + line);
                    }
                    in.close();
                }

            } catch (IOException ex2){
                System.out.println("Verbindung fehlgeschlagen");
                ex2.printStackTrace();

            }
        }
    }

}
