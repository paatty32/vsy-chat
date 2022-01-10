package frontend;

import backend.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatFenster implements ActionListener, Runnable {

    private Client client;

    private JTextArea chatFenster;
    private JTextField chatSchreibFenster;
    private String name;
    private JFrame chat = new JFrame();
    public ChatFenster(Client client){

        this.client = client;

    }

    public void start(){
        this.client.connect(2000);
        this.inputNameFrame();
        this.setFenster();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String nachricht = this.chatSchreibFenster.getText();
        client.writetoServer(name + ": " + nachricht);
        //Säubere Eingabefeld
        this.chatSchreibFenster.setText("");

    }
    public void setFenster(){

        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.chatFenster = new JTextArea();
        this.chatSchreibFenster = new JTextField();
        JScrollPane scrollPane = new JScrollPane(chatFenster);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.chatSchreibFenster.addActionListener(this);

        chat.add(BorderLayout.SOUTH, chatSchreibFenster);
        //chat.add(BorderLayout.CENTER, scrollPane);
        chat.add(BorderLayout.CENTER, chatFenster);


    }

    public void inputNameFrame(){
        JFrame inputName = new JFrame();

        //Fenster
        JTextArea inputNameDialogWindow = new JTextArea();
        //Eingabe Feld
        JTextField inputNameTextField = new JTextField();

        inputNameDialogWindow.append("Geben Sie Ihren Namen ein");
        //innere Klasse
        inputNameTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               name = inputNameTextField.getText();

               //bereite beim enter drücken, das Chatfenster vor
                chat.setSize(300, 400);
                chat.setVisible(true);

                inputName.dispose(); //schließen des fensters
            }
        });

        inputName.add(BorderLayout.CENTER, inputNameDialogWindow);
        inputName.add(BorderLayout.SOUTH, inputNameTextField);

        inputName.setSize(200, 200);
        inputName.setVisible(true);

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
        String line;
        while (true) {
            try {
                if (((line = client.recieveFromServer()) != null)){
                    this.chatFenster.append(line + "\n");
                }
            } catch (IOException e) {
                //e.printStackTrace();
                this.client.connect(2000);
            }
        }
    }

}
