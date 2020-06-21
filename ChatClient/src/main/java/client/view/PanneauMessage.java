package main.java.client.view;

import main.java.client.controller.ChatClientController;
import main.java.client.model.MessageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanneauMessage extends JPanel implements MessageListener {
    private final ChatClientController client_ctrl;
    private final String login;

    private DefaultListModel<String> listeMessagesModel = new DefaultListModel<>();
    private JList<String> listeMessages = new JList<>(listeMessagesModel);
    private JTextField inputMessage = new JTextField();

    public DefaultListModel<String> getListeMessagesModel() {
        return this.listeMessagesModel;
    }

    public PanneauMessage(final ChatClientController client, final String login) {
        client_ctrl = client;
        this.login = login;

        client_ctrl.addMessageListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(listeMessages), BorderLayout.CENTER);
        add(inputMessage, BorderLayout.SOUTH);

        try {
            client_ctrl.downloadHistoryFrom(this.login);
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = inputMessage.getText();
                    if(text.startsWith("/delete")) {
                        client_ctrl.deleteHistoryFrom(login);
                        listeMessagesModel.removeAllElements();
                    } else {
                        client_ctrl.msg(login, text);
                        listeMessagesModel.addElement("Vous: " + text);
                    }
                    inputMessage.setText(""); // On vide le formulaire une fois envoyé
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(String fromLogin, String msgBody) {
        if (fromLogin.equals(this.login) || fromLogin.equals("Vous")) { // Si c'est bien la bonne fenêtre de la bonne personne
            String ligne;
            if (fromLogin.startsWith("#")) { // Si c'est un groupe
                String actualLogin = msgBody.split(" ")[0];
                String actualBody = msgBody.replace(actualLogin + " ", "");
                if (actualLogin.equals(client_ctrl.getMyLogin())) { // Si c'est notre propre message, on ne l'affiche pas
                    return;
                }
                ligne = actualLogin + ": " + actualBody;
            } else {
                ligne = fromLogin + ": " + msgBody;
            }
            this.listeMessagesModel.addElement(ligne);
        }
    }
}
