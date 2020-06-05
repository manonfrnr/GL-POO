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
            if ("quitter".equalsIgnoreCase(line)){
                break;
            }
            String msg = "tu as ecris: " + line + "\n";
            outputStream.write(msg.getBytes());
        }
        clientSocket.close();
    }
}
