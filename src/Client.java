import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Timer;

public class Client {

    public static void main (String [] args){

        /*
        JFrame chat = new JFrame();
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea chatFenster = new JTextArea();
        JTextField chatSchreibFenster = new JTextField();
*/

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
                               // String eingabe = tastaturIn.readLine();
                                /*
                                chatSchreibFenster.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        chatFenster.append(eingabe);
                                        chatSchreibFenster.setText("");
                                    }
                                }); */
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
    /*
            chat.add(BorderLayout.SOUTH, chatSchreibFenster);
            chat.add(BorderLayout.CENTER, chatFenster);

            chat.setSize(300, 400);
            chat.setVisible(true);
            */

            while(true){

                //Lesen vom Server -> es wird nur gelesen, wenn auch etwas drin ist
                while(((line = in.readLine())!= null)){
                    System.out.println(">>: " + line);
                  //  chatFenster.append(line);
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
