package server;

import server.model.Client;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final ConnectionListener listener;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public Connection(Socket socket, ConnectionListener listener) throws IOException {
        this.socket = socket;
        this.listener = listener;

        InputStream is = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is));

        OutputStream os = socket.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(os));


        new Thread(() -> {
            Client client = new Client(Connection.this);
            listener.onConnect(client);
            try {
                while (!socket.isClosed()) {
                    String message = reader.readLine();
                    listener.onMessage(client, message);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());;
            }
        }).start();
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public Socket getSocket() {
        return socket;
    }
}
