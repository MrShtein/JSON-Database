package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import client.Data;
import client.argsWorker.Args;
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
                try (Socket socket = serverSocket.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String msgFromClient = input.readUTF();

                    Args request = gson.fromJson(msgFromClient, Args.class);
                    StringData data = new StringData(request.getCellIndex(), request.getValue());
                    String type = request.getType();

                    switch (type) {
                        case "set":
                            setData(data, output);
                            break;
                        case "get":
                            getData(data, output);
                            break;
                        case "delete":
                            deleteData(data, output);
                            break;
                        case "exit":
                            isWork = false;
                            output.writeUTF(gson.toJson(new DeleteOkResponse("OK")));
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

    public void setData(Data data, DataOutputStream output) throws IOException {
        Data dataObject = checkDataAndReturnCorrectJsonObject(data);
        Response response = new Response(dataDaoIml.setDataToDb(dataObject));
        output.writeUTF(gson.toJson(response));
    }

    public void getData(Data data, DataOutputStream output) throws IOException {
        try {
            String response = dataDaoIml.getDataFromDb(data.getKey());
            GetOkResponse getOkResponse = new GetOkResponse("OK", response);
            output.writeUTF(gson.toJson(getOkResponse));
        } catch (NullPointerException e) {
            ErrorResponse getErrorResponse = new ErrorResponse("ERROR", "No such key");
            output.writeUTF(gson.toJson(getErrorResponse));
        }
    }

    public void deleteData(Data data, DataOutputStream output) throws IOException {
        try {
            String response = dataDaoIml.deleteDataFromDb(data.getKey());
            DeleteOkResponse getOkResponse = new DeleteOkResponse("OK");
            output.writeUTF(gson.toJson(getOkResponse));
        } catch (Exception e) {
            ErrorResponse getErrorResponse = new ErrorResponse("ERROR", e.getMessage());
            output.writeUTF(gson.toJson(getErrorResponse));
        }
    }

    public Data checkDataAndReturnCorrectJsonObject(Data data) {
        StringData stringData = (StringData) data;
        String value = stringData.getValue();
        boolean isBoolean = "true".equalsIgnoreCase(value)
                || "false".equalsIgnoreCase(value);
        boolean isInteger = value.matches("\\d+");
        JsonObject jsonObject = new JsonObject();
        if (isBoolean) {
            return new BooleanData(data.getKey(), Boolean.parseBoolean(stringData.getValue()));
        } else if (isInteger) {
            return new IntegerData(data.getKey(), Integer.parseInt(stringData.getValue()));
        } else {
            return data;
        }
    }
}
