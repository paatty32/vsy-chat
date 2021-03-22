package gui;

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


    /*Zum lesen*/
    @Override
    public void run() {
        String line = null;
        while (true) {
            try {
                if (((line = client.recieveFromServer()) != null)){
                    this.chatFenster.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String text = this.chatSchreibFenster.getText();
        client.writetoServer(text);
        this.chatFenster.append(text);
        this.chatSchreibFenster.setText("");

    }
    public void setFenster(){
        JFrame chat = new JFrame();
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.chatFenster = new JTextArea();
        this.chatSchreibFenster = new JTextField();
        this.chatSchreibFenster.addActionListener(this);

        chat.add(BorderLayout.SOUTH, chatSchreibFenster);
        chat.add(BorderLayout.CENTER, chatFenster);

        chat.setSize(300, 400);
        chat.setVisible(true);
    }

    public static void main(String args[]){
            Client client = new Client("localhost", 2000);
            ChatFenster chatFenster = new ChatFenster(client);

            chatFenster.setFenster();
            chatFenster.run();


    }



}
