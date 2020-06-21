package main.java.client.view;

import main.java.client.controller.ChatClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static main.java.client.view.ConstantsClient.*;



public class FenetreConnexion extends JFrame {
    private final ChatClientController client_controller;
    JTextField login = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton bouton = new JButton("Connexion");

    public FenetreConnexion() {
        super("Connexion");
        
        client_controller = new ChatClientController(this, HOST_NAME, PORT_ID);
        try {
            client_controller.connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter au serveur : " + e.getLocalizedMessage());
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(300, 200));

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(login);
        p.add(password);
        p.add(bouton);

        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER);

        pack();
    }

    public void doLogin() {
        String login_value = this.login.getText();
        char[] password_value = this.password.getPassword();
        client_controller.login(login_value, String.valueOf(password_value));
    }

    public boolean okStartUserPanel()
    {
        PanneauUtilisateur panneauUtilisateur = new PanneauUtilisateur(client_controller);
        JFrame frame = new JFrame("Liste des Utilisateurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 600));

        frame.getContentPane().add(panneauUtilisateur, BorderLayout.CENTER);
        frame.setVisible(true);
        this.setVisible(false);
        return true;
    }

    public boolean errorStartUserPanel()
    {
        JOptionPane.showMessageDialog(this, "Login ou mot de passe incorrect");
        return false;
    }

    public static void main(String[] args) {
        FenetreConnexion fenetreConnexion = new FenetreConnexion();
        fenetreConnexion.setVisible(true);
    }
}
