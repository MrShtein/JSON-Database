package client;

import com.beust.jcommander.JCommander;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import client.argsWorker.Args;

public class Client extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;
    private final String[] args;

    public Client(String[] args) {
        this.args = args;
    }

    public void run() {
        Args arguments = new Args();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);
        System.out.println("Client started");
        try (Socket socket = new Socket(InetAddress.getByName(IP), PORT)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String msgToServer = arguments.toString();
            output.writeUTF(msgToServer);

            String msg = input.readUTF();
            System.out.printf("Sent: %s\n", msgToServer.replaceAll("\\+", " ").trim());
            System.out.printf("Received: %s\n", msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
