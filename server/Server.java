package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;

    @Override
    public void run() {
        super.run();
        System.out.println("Server started!");
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(IP))) {
            try (Socket socket = serverSocket.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                String msgFromClient = input.readUTF();
                String msgForClient = "A record # 12 was sent!";
                System.out.printf("Received: %s\nSent: %s\n", msgFromClient, msgForClient);
                output.writeUTF(msgForClient);

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
