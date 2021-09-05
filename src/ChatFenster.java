import backend.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatFenster implements ActionListener, Runnable {

    private Client client;

    JTextArea chatFenster;
    JTextField chatSchreibFenster;

    public ChatFenster(Client client){

        this.client = client;


    }

    public void start(){
        this.client.connect(2000);
        this.setFenster();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String text = this.chatSchreibFenster.getText();
        client.writetoServer(text);
        //this.chatFenster.append(text);
        this.chatSchreibFenster.setText("");

    }
    public void setFenster(){
        JFrame chat = new JFrame();

        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.chatFenster = new JTextArea();
        this.chatSchreibFenster = new JTextField();
        JScrollPane scrollPane = new JScrollPane(chatFenster);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.chatSchreibFenster.addActionListener(this);

        chat.add(BorderLayout.SOUTH, chatSchreibFenster);
        //chat.add(BorderLayout.CENTER, scrollPane);
        chat.add(BorderLayout.CENTER, chatFenster);

        chat.setSize(300, 400);
        chat.setVisible(true);
    }

    public static void main(String args[]){
            Client client = new Client("localhost", 2000);
            ChatFenster chatFenster = new ChatFenster(client);

            chatFenster.start();

            /*Thread erzeugen um im Hintergrund lesen zu können */
            Thread thread = new Thread(chatFenster);
            thread.start();

    }

    /*Zum lesen*/
    @Override
    public void run() {
        String line = null;
        while (true) {
            try {
                if (((line = client.recieveFromServer()) != null)){
                    this.chatFenster.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
