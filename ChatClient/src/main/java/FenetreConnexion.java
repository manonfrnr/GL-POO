import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;

public class FenetreConnexion extends JFrame {
    private final ChatClient client;
    JTextField login = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton bouton = new JButton("Connexion");

    public FenetreConnexion() {
        super("Connexion");
        
        this.client = new ChatClient("localhost", 8818);
        try {
            client.connect();
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

    private void doLogin() {
        String login_value = this.login.getText();
        String password_value = this.password.getText();

        try {
            if(client.login(login_value, password_value)) {
                PanneauUtilisateur panneauUtilisateur = new PanneauUtilisateur(client);
                JFrame frame = new JFrame("Liste des Utilisateurs");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(new Dimension(400, 600));

                frame.getContentPane().add(panneauUtilisateur, BorderLayout.CENTER);
                frame.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Login ou mot de passe incorrect");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FenetreConnexion fenetreConnexion = new FenetreConnexion();
        fenetreConnexion.setVisible(true);
    }
}
