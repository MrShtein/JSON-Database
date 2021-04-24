package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;

    @Override
    public void run() {
        System.out.println("Client started");
        super.run();
        try (Socket socket = new Socket(InetAddress.getByName(IP), PORT)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String msgToServer = "Give me a record # 12";
            output.writeUTF(msgToServer);
            String msg = input.readUTF();
            System.out.printf("Sent: %s\n", msgToServer);
            System.out.printf("Received: %s\n", msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
