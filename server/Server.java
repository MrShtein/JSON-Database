package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

public class Server extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;

    @Override
    public void run() {
        super.run();
        System.out.println("Server started!");
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(IP))) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String msg = input.readUTF();
                    int num = Integer.parseInt(Pattern.compile("\\d+").matcher(msg).group());
                    System.out.printf("Received: Give me a record # %d", num);
                    output.writeUTF(String.format("Sent: A record # %d was sent!", num));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}