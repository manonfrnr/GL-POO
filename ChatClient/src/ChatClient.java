import org.apache.commons.lang.StringUtils;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;
    private String mylogin;

    private ArrayList<UserStatus> userSatus = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] arg) throws IOException {
        ChatClient client = new ChatClient("localhost", 8818);
        client.addUserStatus(new UserStatus() {

            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
            }
        });

        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("Vous avez reçu un message de " + fromLogin + "====>" + msgBody);
            }
        });

        if (!client.connect()) {
            System.err.println("Connect Failed");
        } else {
            System.out.println("Connect Successful");
            if (client.login("invit", "invit")) {
                System.out.println("Login successful");

                client.msg("test", "Coucou toi!");
            } else {
                System.err.println("Login failed");
            }
        }
        //client.logoff();
    }

    public void msg(String SendTo, String msgBody) throws IOException {
        String cmd = "msg " + SendTo + " " + msgBody + "\n";
        serverOut.write(cmd.getBytes());
    }

    public void join(String group) throws IOException {
        String cmd = "join " + group + "\n";
        serverOut.write(cmd.getBytes());
    }

    public void leave(String group) throws IOException {
        String cmd = "leave " + group + "\n";
        serverOut.write(cmd.getBytes());
    }

    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response Line:" + response );

        if ("ok connection".equalsIgnoreCase(response)) {
            this.mylogin = login;
            startMessage();
            return true;
        } else {
            return false;
        }
    }

    public String getMyLogin() {
        return this.mylogin;
    }

    private void logoff() throws IOException {
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    private void startMessage() {
        Thread t = new Thread(){
            @Override
            public void run(){
                readMesssageLoop();
            }

        };
        t.start();
    }

    private void readMesssageLoop() {

        try {
            String line;
            while( (line = bufferedIn.readLine())!= null) {
                String[] tokens = StringUtils.split(line);

                if (tokens != null && tokens.length > 0){
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)){
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        handleOffline(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)){
                        String[] tokensMessage = StringUtils.split(line, null, 3);
                        handleMessage(tokensMessage);
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(String[] tokensMessage) {
        String login = tokensMessage [1];
        String msgBody = tokensMessage[2];

        for(MessageListener listener : messageListeners){
            listener.onMessage(login, msgBody);
        }
    }

    private void handleOffline(String[] tokens) {
        System.out.println(tokens[1] + " est en hors ligne");
        String login = tokens[1];
        for (UserStatus listener : userSatus){
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        System.out.println(tokens[1] + " est en ligne");
        String login = tokens[1];
        for (UserStatus listener : userSatus){
            listener.online(login);
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Client port is" + socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }


    public void addUserStatus(UserStatus listener){
        userSatus.add(listener);
    }
    public void removeUserStatus(UserStatus listener){
        userSatus.remove(listener);
    }

    public void addMessageListener(MessageListener listener){
        messageListeners.add(listener);
    }
    public void removeMessageListener(MessageListener listener){
        messageListeners.remove(listener);
    }

    public void downloadHistoryFrom(String login) throws IOException {
        String cmd = "history " + login + "\n";
        serverOut.write(cmd.getBytes());
    }
}
