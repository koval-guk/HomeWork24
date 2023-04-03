package server.model;

import server.Connection;

import java.net.Socket;
import java.time.LocalTime;

public class Client {
    private final String clientName;
    private final LocalTime timeOfConnect;
    private final Socket clientSocket;
    private final Connection connection;
    private static int id;

    public Client(Connection connection) {
        this.connection = connection;
        this.clientName = "client_" + (++id);
        this.timeOfConnect = LocalTime.now();
        this.clientSocket = connection.getSocket();
    }

    public String getClientName() {
        return clientName;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "Client{" + "Name=" + clientName + "| timeOfConnect=" + timeOfConnect + "| Socket=" + clientSocket + "}";
    }
}
