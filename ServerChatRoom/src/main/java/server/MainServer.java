package main.java.server;

import main.java.server.controller.Server;

public class MainServer {
    public static void main(String[] args){
        Server server = Singletons.getServer();
        server.start();
    }
}
