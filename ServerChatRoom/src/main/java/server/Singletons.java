package main.java.server;

import main.java.server.controller.Server;

import static main.java.server.ConstantsServer.PORT_ID;

public class Singletons {

    private static Server serverInstance = null;

    public static Server getServer() {
        if (serverInstance == null) {
            serverInstance = new Server(PORT_ID);
        }
        return serverInstance;
    }
}
