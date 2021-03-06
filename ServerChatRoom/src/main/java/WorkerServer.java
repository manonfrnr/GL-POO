import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class WorkerServer extends Thread{

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private HashSet <String> topicSet = new HashSet<>();

    public WorkerServer(Server server, Socket clientSocket) {
        this.server = server;
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
        try {
            InputStream inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = StringUtils.split(line);

                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("logoff".equals(cmd) || "quitter".equalsIgnoreCase(cmd)) {
                        gestionLogoff();
                        break;
                    } else if ("login".equalsIgnoreCase(cmd)) {
                        gestionLogin(outputStream, tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        String[] tokensMessage = StringUtils.split(line, null, 3);
                        gestionMessage(tokensMessage);
                    } else if ("join".equalsIgnoreCase(cmd)) {
                        gestionJoin(tokens);
                    } else if ("leave".equalsIgnoreCase(cmd)) {
                        gestionLeave(tokens);
                    } else if ("history".equalsIgnoreCase(cmd)) {
                        gestionHistory(tokens);
                    } else if ("delete".equalsIgnoreCase(cmd)) {
                        gestionDelete(tokens);
                    } else {
                        String msg = "inconnu " + cmd + "\n";
                        outputStream.write(msg.getBytes());
                    }
                }
            }
            clientSocket.close();
        } catch (SocketException e) {
            System.out.println("Une erreur est survenue chez le client " + login + ", déconnexion");
            gestionLogoff();
        }

    }

    private void gestionHistory(String[] tokens) {
        if (tokens.length > 1) {
            String from = tokens[1];
            String messageAEnvoyer = "";
            for(History h : server.getHistoriques()) {
                if(from.startsWith("#")) { // Si c'est un groupe
                    if (h.getTo().equalsIgnoreCase(from)) {
                        if (h.getFrom().equals(this.login)) {
                            messageAEnvoyer = "msg " + from + " Vous " + h.getMessage() + "\n";
                        } else {
                            messageAEnvoyer = "msg " + from + " " + h.getFrom() + " " + h.getMessage() + "\n";
                        }
                    }
                } else {
                    if (h.getFrom().equals(from) && h.getTo().equals(this.login)) {
                        messageAEnvoyer = "msg " + h.getFrom() + " " + h.getMessage() + "\n";
                    } else if (h.getFrom().equals(this.login) && h.getTo().equals(from)) {
                        messageAEnvoyer = "msg Vous " + h.getMessage() + "\n";
                    }
                }
                try {
                    envoyer(messageAEnvoyer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void gestionLeave(String[] tokens) {
        if(tokens.length > 1){
            String topic = tokens [1];
            topicSet.remove(topic);
            try {
                List<WorkerServer> listeWorker = server.getWorkerList();
                for(WorkerServer ws : listeWorker) {
                    if (ws.estMembreDuTopic(topic)) {
                        ws.envoyer("msg " + topic + " Server " + login + " leaved the channel " + topic + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean estMembreDuTopic(String topic){
        return topicSet.contains(topic);
    }

    private void gestionJoin(String[] tokens) {
        if(tokens.length > 1){
            String topic = tokens [1];
            topicSet.add(topic);
            try {
                List<WorkerServer> listeWorker = server.getWorkerList();
                for(WorkerServer ws : listeWorker) {
                    if (ws.estMembreDuTopic(topic)) {
                        ws.envoyer("msg " + topic + " Server " + login + " joined the channel " + topic + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void gestionMessage(String[] tokens) throws IOException {
        String receveur = tokens[1];
        String message = tokens[2];
        System.out.println("Message " + message);

        server.getHistoriques().add(new History(this.login, receveur, message));

        boolean isTopic = receveur.charAt(0) == '#';

        List<WorkerServer> listeWorker = server.getWorkerList();
        for(WorkerServer ws : listeWorker) {
            if(isTopic) {
                if (ws.estMembreDuTopic(receveur)) {
                    String messageAEnvoyer = "msg " + receveur + " " + login + " " + message + "\n";
                    ws.envoyer(messageAEnvoyer);
                }
            } else {
                if(receveur.equalsIgnoreCase(ws.getLogin())) {
                    String messageAEnvoyer = "msg " + login + " " + message + "\n";
                    ws.envoyer(messageAEnvoyer);
                }
            }
        }
    }

    private void gestionLogoff() throws IOException {
        server.removeWorker(this);
        List<WorkerServer> workerList = server.getWorkerList();

        //notifie les autres utilisateur de la déconnexion de l'utilisateur
        String onlineMessage = "offline " + login + "\n";
        for(WorkerServer workerServer : workerList){
            if (!login.equals(workerServer.getLogin())) { //éviter d'afficher son propre statut
                workerServer.envoyer(onlineMessage);
            }
        }
        clientSocket.close();
    }

    public String getLogin(){
        return login;
    }

    private void gestionLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3){
            String login = tokens[1];
            String password = tokens[2];

            if (login.equals("invit") && password.equals("invit") || login.equals("test") && password.equals("test") ){ //les deux utilisateurs possible pour la connexion (pour le moment)
                String msg = "ok connection\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("L'utilistauer s'est connecté avec succès: " + login);

                List<WorkerServer> workerList = server.getWorkerList();

                // renvoie à l'utilisateur courant tous les autres utilisateurs en ligne
                for(WorkerServer workerServer : workerList){
                       if (workerServer.getLogin() != null) {
                           if (!login.equals(workerServer.getLogin())) { //éviter d'afficher son propre statut
                               String message2 = "online " + workerServer.getLogin() + "\n";
                               envoyer(message2);
                           }
                       }
                }

                // renvoie aux autres utilisateurs les utilisateurs en ligne
                String onlineMessage = "online " + login + "\n";
                for(WorkerServer workerServer : workerList){
                    if (!login.equals(workerServer.getLogin())) { //éviter d'afficher son propre statut
                        workerServer.envoyer(onlineMessage);
                    }
                }
            }else{
                String msg = "erreur connection\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    private void gestionDelete(String tokens[]) {
        if (tokens.length > 1) {
            String destinataire = tokens[1];
            server.deleteHistory(destinataire, this.login);
        }
    }

    private void envoyer(String message) throws IOException {
        if (login != null && outputStream != null){
            outputStream.write(message.getBytes());
        }
    }
}
