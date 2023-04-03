package server;

import server.model.Client;

public interface ConnectionListener {
    void onConnect(Client client);
    void onMessage(Client client, String message);
    void onDisconnect(Client client);
}
