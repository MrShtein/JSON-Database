package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;
    boolean isWork = true;

    @Override
    public void run() {
        super.run();
        System.out.println("Server started!");
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



