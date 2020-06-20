import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ClientGUITest {
    private ChatClient cc, cc2;
    private Server s;

    @Test
    public void connectionTest() throws IOException, InterruptedException {
        s = new Server(8818);
        s.start();
        FenetreConnexion fc = new FenetreConnexion();
        fc.login.setText("invit");
        fc.password.setText("invit");
        assertTrue(fc.doLogin());
        s.stopMe();
    }

    @Test
    public void messageTest() throws IOException, InterruptedException {
        s = new Server(8818);
        s.start();
        ChatClient cc = new ChatClient("localhost", 8818);
        cc.connect();
        cc.login("test", "test");
        ChatClient cc2 = new ChatClient("localhost", 8818);
        cc2.connect();
        cc2.login("invit", "invit");
        PanneauMessage pm = new PanneauMessage(cc, "invit");
        cc2.msg("test", "bonjour");
        TimeUnit.SECONDS.sleep(1);
        assertTrue(pm.getListeMessagesModel().contains("invit: bonjour"));
        cc2.logoff();
        s.stopMe();
    }

    @Test
    public void utilisateurTest() throws IOException {
        s = new Server(8818);
        s.start();
        ChatClient cc = new ChatClient("localhost", 8818);
        cc.connect();
        cc.login("test", "test");
        PanneauUtilisateur pu = new PanneauUtilisateur(cc);
        assertTrue(pu.openMessageWindow("invit"));
        s.stopMe();
    }
}