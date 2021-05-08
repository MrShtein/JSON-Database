package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.Db.DataDaoIml;
import server.Db.Db;

public class Server extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;
    boolean isWork = true;
    DataDaoIml dataDaoIml = new DataDaoIml();
    Gson gson = new Gson();

    @Override
    public void run() {
        super.run();
        System.out.println("Server started!");
        Db db = new Db();
        while (isWork) {
            try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(IP))) {
                Socket socket = serverSocket.accept();

                ExecutorService executor = Executors.newFixedThreadPool(8);
                executor.execute(new WorkWithTasks(this, socket));
                executor.shutdown();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}



