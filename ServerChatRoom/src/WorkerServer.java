import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;


public class WorkerServer extends Thread{

    private final Socket clientSocket;
    private String login = null;

    public WorkerServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run (){
        try {
            gestionClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gestionClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);

            if (tokens != null && tokens.length > 0){
                String cmd = tokens[0];
                if ("quitter".equalsIgnoreCase(cmd)){
                    break;
                }else if ("login".equalsIgnoreCase(cmd)){
                    GestionLogin(outputStream, tokens);
                }else{
                    String msg = "inconnu " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }

    private void GestionLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3){
            String login = tokens[1];
            String password = tokens[2];

            if (login.equals("invit") && password.equals("invit") || login.equals("test") && password.equals("test") ){
                String msg = "ok connection\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("L'utilistauer s'est connecté avec succès: " + login);
            }else{
                String msg = "erreur connection\n";
                outputStream.write(msg.getBytes());
            }
        }
    }
}
