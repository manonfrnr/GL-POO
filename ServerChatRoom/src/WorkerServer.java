import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;


public class WorkerServer extends Thread{

    private final Socket clientSocket;

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

    //comment

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
                }else{
                    String msg = "inconnu " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }
}
