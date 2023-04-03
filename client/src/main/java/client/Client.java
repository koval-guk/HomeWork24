package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {
    private Socket clientSocket;

    public Client(String host, int port) {

        try {
            clientSocket = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("connection refused");;
        }
        try(
             InputStream is = clientSocket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             Scanner scanner = new Scanner(System.in);
             OutputStream os = clientSocket.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(os))){
            String response;
            String message = null;

            while (true) {
                response = reader.readLine();
                System.out.println("[server:] " + response);
                if (response.equals("disconnected")) {
                    break;
                } else if (response.equals("file")) {
                    System.out.println("test message : " + message);
                    assert message != null;
                    String[] path = message.split(" ");
                    sendFile(new File(path[1]));
                    System.out.println(clientSocket.isClosed());
                }
                if (!reader.ready()) {
                    System.out.print("Input : ");
                    message = scanner.nextLine();
                    writer.println(message);
                    writer.flush();
                }
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }

    public void sendFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file.getPath());
             BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());

            byte[] byteArray = new byte[10];
            long rest = file.length();
            while (rest > 0) {
                int part = fis.read(byteArray);
                bos.write(byteArray, 0, part);
                rest -= part;
            }
            bos.flush();
            fis.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found !!!");
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }


    @Override
    public void close() throws Exception {
        if (clientSocket.isClosed()) {
            return;
        }
        clientSocket.close();
    }
}
