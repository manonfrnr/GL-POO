import java.io.IOException;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MainServer {
    public static void main(String[] args){
        int port = 8818;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                System.out.println("About to accept client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                WorkerServer workerServer = new WorkerServer(clientSocket);
                workerServer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
