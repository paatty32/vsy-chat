package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BackUpServer implements Runnable{
    public static ArrayList<Socket> clientList = new ArrayList<>();

        public static void main(String args[]) {
            int port = 2001;
            try {
                ServerSocket serSocket = new ServerSocket(port);
                System.out.println("Server lauscht auf Port: " + port);
                while(true){
                    Socket clientSocket = serSocket.accept();
                    clientList.add(clientSocket);
                    Thread t = new Thread(new BackUpServer(clientSocket)); //Für jeden neuen Client wird ein Thread gestartet
                    t.start();
                }
            } catch (IOException ex) {
                System.out.println("Socket konnte nicht gehorcht werden.");
                ex.printStackTrace();
            }

        }

        private Socket clientSocket;
        //private ArrayList<Socket> clientList = null;

        private BackUpServer(Socket clientSocket){

            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {

            try {
                InputStreamReader socketStream = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader in = new BufferedReader(socketStream);
                System.out.println("TEST");

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

                String line;
                while((line = in.readLine()) != null){
                    System.out.println("TEST2");

                    System.out.println("Server: gelsen vom Client= "+line);

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

