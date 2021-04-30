package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import server.Db.DataDaoIml;

public class Server extends Thread {

    private final String IP = "127.0.0.1";
    private final int PORT = 23456;
    boolean isWork = true;
    DataDaoIml dataDaoIml = new DataDaoIml();


    @Override
    public void run() {
        super.run();
        System.out.println("Server started!");
        while (isWork) {
            try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(IP))) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String msgFromClient = input.readUTF();
                    DataFromMessage dataFromMessage = new MessageHandler(msgFromClient).handleMessage();

                    switch (dataFromMessage.getAction()) {
                        case "set":
                            setData(dataFromMessage, output);
                            break;
                        case "get":
                            getData(dataFromMessage, output);
                            break;
                        case "delete":
                            deleteData(dataFromMessage, output);
                            break;
                        case "exit":
                            isWork = false;
                            output.writeUTF("OK");
                            break;
                        default:
                            throw new IllegalArgumentException("ERROR");
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setData(DataFromMessage data, DataOutputStream output) throws IOException {
       output.writeUTF(dataDaoIml.setDataToDb(data.getIndex(), data.getValue()));
    }

    public void getData(DataFromMessage data, DataOutputStream output) throws IOException {
        output.writeUTF(dataDaoIml.getDataFromDb(data.getIndex()));
    }

    public void deleteData(DataFromMessage data, DataOutputStream output) throws IOException {
        output.writeUTF(dataDaoIml.deleteDataFromDb(data.getIndex()));
    }
}
