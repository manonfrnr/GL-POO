import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class PanneauUtilisateur extends JPanel implements UserStatus {

    private final ChatClient client;
    private JList<String> UIlist;
    private DefaultListModel<String> UIlistModel;
    private JTextField champDestinataire;

    public PanneauUtilisateur(ChatClient client) {
        this.client = client;
        this.client.addUserStatus(this);
        this.UIlistModel = new DefaultListModel<>();
        this.UIlist = new JList<>(UIlistModel);
        this.champDestinataire = new JTextField();
        setLayout(new BorderLayout());
        add(new JScrollPane(UIlist), BorderLayout.CENTER);
        add(champDestinataire, BorderLayout.SOUTH);

        this.UIlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Si double clique
                    String login = UIlist.getSelectedValue();
                    openMessageWindow(login);
                }
            }
        });

        champDestinataire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(champDestinataire.getText().startsWith("#")) { // Si c'est un groupe
                    try {
                        client.join(champDestinataire.getText());
                        openMessageWindow(champDestinataire.getText());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (UIlistModel.contains(champDestinataire.getText())) {
                        openMessageWindow(champDestinataire.getText());
                    } else {
                        JOptionPane.showMessageDialog(getParent(), "Cet utilisateur n'existe pas. Si vous souhaitez rejoindre un groupe, ajouter un # au début du nom.");
                    }
                }
            }
        });
    }

    private void openMessageWindow(String login) {
        PanneauMessage panneauMessage = new PanneauMessage(client, login);

        JFrame frame = new JFrame("Messages à " + login);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(500, 500));
        frame.getContentPane().add(panneauMessage, BorderLayout.CENTER);
        frame.setVisible(true);
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
