import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    private Server server;

    @Test
    public void testListesVides() throws IOException {
        server = new Server(8818);
        server.start();
        assertEquals(server.getWorkerList().size(), 0);
        assertEquals(server.getHistoriques().size(), 0);
        server.stopMe();
    }

    @Test
    public void testNouvelHistorique() throws IOException {
        server = new Server(8818);
        server.start();
        server.getHistoriques().add(new History("from", "to", "message"));
        assertEquals(server.getHistoriques().get(0).getFrom(), "from");
        assertEquals(server.getHistoriques().get(0).getTo(), "to");
        assertEquals(server.getHistoriques().get(0).getMessage(), "message");
        server.stopMe();
    }

    @Test
    public void testDeleteHistorique() throws IOException {
        server = new Server(8818);
        server.start();
        server.getHistoriques().add(new History("user1", "user2", "message1"));
        server.getHistoriques().add(new History("user1", "user3", "message2"));
        server.getHistoriques().add(new History("user1", "user2", "message3"));
        server.deleteHistory("user1", "user2");
        assertEquals(server.getHistoriques().get(0).getMessage(), "message2");
        server.stopMe();
    }

}
