import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private final int serverPort;

    private ArrayList<History> historiques;

    private ArrayList<WorkerServer> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
        this.historiques = new ArrayList<>();
    }

    public List<WorkerServer> getWorkerList(){
        return workerList;
    }

    public ArrayList<History>  getHistoriques() {
        return this.historiques;
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

    public void removeWorker(WorkerServer workerServer) {
        workerList.remove(workerServer);
    }
}
