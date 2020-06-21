package main.java.server.controller;

import main.java.server.model.History;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private final int serverPort;
    private Boolean stop = false;
    private ServerSocket serverSocket;

    private ArrayList<History> historiques;

    private ArrayList<WorkerServer> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
        this.historiques = new ArrayList<>();
    }

    public void deleteHistory(String user1, String user2) {
        if (user1.startsWith("#") || user2.startsWith("#")) {
            String groupname = (user1.startsWith("#") ? user1 : user2);
            this.historiques.removeIf(h -> h.getFrom().equals(groupname) || h.getTo().equals(groupname));
        } else {
            this.historiques.removeIf(h -> h.getFrom().equals(user1) && h.getTo().equals(user2) || h.getFrom().equals(user2) && h.getTo().equals(user1));
        }
    }

    public List<WorkerServer> getWorkerList(){
        return workerList;
    }

    public ArrayList<History>  getHistoriques() {
        return this.historiques;
    }

    public void stopMe() throws IOException {
        this.stop = true;
        if (this.serverSocket != null) {
            this.serverSocket.close();
        }
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(serverPort);
            while(!stop){
                System.out.println("About to accept client connection...");
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Accepted connection from " + clientSocket);
                    WorkerServer workerServer = new WorkerServer(this, clientSocket);
                    workerList.add(workerServer);
                    workerServer.start();
                } catch (SocketException e) {

                }
            }
            System.out.println("Bye");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(WorkerServer workerServer) {
        workerList.remove(workerServer);
    }
}
