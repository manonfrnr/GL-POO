package main.java.server.model;

public class History {
    private String from;
    private String message;
    private String to;

    public History(String from, String to, String message) {
        this.from = from;
        this.message = message;
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTo() {
        return this.to;
    }
}
