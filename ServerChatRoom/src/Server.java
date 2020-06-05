import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private final int serverPort;

    private ArrayList<WorkerServer> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<WorkerServer> getWorkerList(){
        return workerList;
    }

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true){
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                WorkerServer workerServer = new WorkerServer(this, clientSocket);
                workerList.add(workerServer);
                workerServer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}