import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    private ChatClient cc, cc2;
    private Server s;

    @Test
    public void connectionTest() throws IOException, InterruptedException {
        s = new Server(8818);
        s.start();
        cc = new ChatClient("localhost", 8818);
        cc.connect();
        cc2 = new ChatClient("localhost", 8818);
        cc2.connect();
        Boolean r1 = cc.login("test", "test");
        Boolean r2 = cc2.login("invit", "invit");
        TimeUnit.SECONDS.sleep(1);
        s.stopMe();
        assertTrue(r1);
        assertTrue(r2);
    }

    @Test
    public void messageTest() throws IOException, InterruptedException {
        s = new Server(8818);
        s.start();
        cc = new ChatClient("localhost", 8818);
        cc.connect();
        cc.login("test", "test");
        cc2 = new ChatClient("localhost", 8818);
        cc2.connect();
        cc2.login("invit", "invit");
        cc.msg("invit", "message");
        TimeUnit.SECONDS.sleep(1);
        assertEquals("message", s.getHistoriques().get(0).getMessage());
        s.stopMe();
    }

    @Test
    public void groupTest() throws IOException, InterruptedException {
        s = new Server(8818);
        s.start();
        cc = new ChatClient("localhost", 8818);
        cc.connect();
        cc.login("test", "test");
        cc.msg("#group", "message2");
        TimeUnit.SECONDS.sleep(1);
        assertEquals(s.getHistoriques().size(), 1);
        cc.join("#group");
        cc.msg("#group", "message3");
        TimeUnit.SECONDS.sleep(1);
        assertTrue(s.getWorkerList().get(0).estMembreDuTopic("#group"));
        assertEquals(s.getHistoriques().get(1).getMessage(), "message3");
        assertEquals(s.getHistoriques().get(1).getTo(), "#group");
        cc.leave("#group");
        TimeUnit.SECONDS.sleep(1);
        assertFalse(s.getWorkerList().get(0).estMembreDuTopic("#group"));
        s.stopMe();
    }

    @Test
    public void deleteTest() throws IOException, InterruptedException {
        s = new Server(8818);
        s.start();
        cc = new ChatClient("localhost", 8818);
        cc.connect();
        cc.login("test", "test");
        assertEquals(0, s.getHistoriques().size());
        cc.msg("test", "message");
        TimeUnit.SECONDS.sleep(1);
        assertEquals(1, s.getHistoriques().size());
        cc.deleteHistoryFrom("test");
        TimeUnit.SECONDS.sleep(1);
        assertEquals(0, s.getHistoriques().size());
        s.stopMe();
    }
}
