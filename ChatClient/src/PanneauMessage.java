import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanneauMessage extends JPanel implements MessageListener {
    private final ChatClient client;
    private final String login;

    private DefaultListModel<String> listeMessagesModel = new DefaultListModel<>();
    private JList<String> listeMessages = new JList<>(listeMessagesModel);
    private JTextField inputMessage = new JTextField();

    public PanneauMessage(ChatClient client, String login) {
        this.client = client;
        this.login = login;

        client.addMessageListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(listeMessages), BorderLayout.CENTER);
        add(inputMessage, BorderLayout.SOUTH);

        inputMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = inputMessage.getText();
                    client.msg(login, text);
                    listeMessagesModel.addElement("Vous: " + text);
                    inputMessage.setText(""); // On vide le formulaire une fois envoyé
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(String fromLogin, String msgBody) {
        if (fromLogin.equals(this.login)) { // Si c'est bien la bonne fenêtre de la bonne personne
            String ligne = fromLogin + ": " + msgBody;
            this.listeMessagesModel.addElement(ligne);
        }
    }
}
