import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanneauUtilisateur extends JPanel implements UserStatus {

    private final ChatClient client;
    private JList<String> UIlist;
    private DefaultListModel<String> UIlistModel;

    public PanneauUtilisateur(ChatClient client) {
        this.client = client;
        this.client.addUserStatus(this);
        this.UIlistModel = new DefaultListModel<>();
        this.UIlist = new JList<>(UIlistModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(UIlist), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8818);
        PanneauUtilisateur panneauUtilisateur = new PanneauUtilisateur(client);
        JFrame frame = new JFrame("Liste des Utilisateurs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 600));

        frame.getContentPane().add(panneauUtilisateur, BorderLayout.CENTER);
        frame.setVisible(true);

        if(client.connect()) {
            try {
                client.login("invit", "invit");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void online(String login) {
        this.UIlistModel.addElement(login);
    }

    @Override
    public void offline(String login) {
        this.UIlistModel.removeElement(login);
    }
}
