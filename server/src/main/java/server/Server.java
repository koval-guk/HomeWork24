package server;

import server.model.Client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server implements AutoCloseable, ConnectionListener {
    private final ServerSocket serverSocket;
    private final List<Client> clients = new ArrayList<>();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();

            Connection connection = new Connection(socket, this);


        }
    }

    @Override
    public void close() throws Exception {
        if (serverSocket.isClosed()) {
            return;
        }
        serverSocket.close();
    }

    @Override
    public void onConnect(Client client) {
        client.getConnection().sendMessage("connected");
        client.getConnection().sendMessage("'-exit' or '-file path_to_file'");
        for (Client clientAtList : clients) {
            clientAtList.getConnection().sendMessage("[server]: " + client.getClientName() + " connected successfully");
        }
        clients.add(client);
        System.out.println(client + " connected successfully");
    }

    @Override
    public void onMessage(Client client, String message) {
        if (message.equalsIgnoreCase("-exit")) {
            onDisconnect(client);
        } else if (message.contains("-file")) {
            client.getConnection().sendMessage("file");
            onFile(message, client);
            client.getConnection().sendMessage("file send");
        }
        client.getConnection().sendMessage("echo : " + message);
        System.out.println(client.getClientName() + " message : " + message);
    }

    @Override
    public void onDisconnect(Client client) {
        try {
            client.getConnection().sendMessage("disconnected");
            client.getConnection().getSocket().close();
            clients.remove(client);
            System.out.println(client + " disconnected");
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }

    public void onFile(String fileString, Client client) {
        String[] strings = fileString.split("[ /]");
        System.out.println(Arrays.toString(strings));
        File file = new File(client.getClientName() + "_" + strings[strings.length - 1]);
        byte[] byteArray = new byte[10];
        try (BufferedInputStream bis = new BufferedInputStream(client.getConnection().getSocket().getInputStream());
             FileOutputStream fos = new FileOutputStream(file)
        ) {
            int part;
            while ((part = bis.read(byteArray)) == 10) {
                fos.write(byteArray);
            }
            fos.write(byteArray, 0, part);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }
}
