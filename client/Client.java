package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

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
        System.out.println("Client started");
        try (Socket socket = new Socket(InetAddress.getByName(IP), PORT)) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());


            String msgToServer = getResponse(args);
            output.writeUTF(msgToServer);

            String msg = input.readUTF();
            System.out.printf("Sent: %s\n", msgToServer);
            System.out.printf("Received: %s\n", msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getResponse(String[] args) {
        Args arguments = new Args();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);
        Gson gson = new Gson();
        return gson.toJson(arguments);
    }
}
