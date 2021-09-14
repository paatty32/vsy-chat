package backend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Timer;

public class Client {

    private InputStreamReader socketStream;
    private BufferedReader in;
    private InputStreamReader tastatur;
    private BufferedReader tastaturIn;
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
