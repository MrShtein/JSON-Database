package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import client.Data;
import client.argsWorker.Args;
import server.Db.DataDaoIml;

public class WorkWithTasks implements Runnable {

    private Server server;
    Gson gson;
    DataDaoIml dataDaoIml;
    Socket socket;


    public WorkWithTasks(Server server, Socket output) {
        this.server = server;
        gson = new Gson();
        dataDaoIml = new DataDaoIml();
        socket = output;
    }

    @Override
    public void run() {

        String message = null;
        DataOutputStream output = null;
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            message = input.readUTF();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        Args request = gson.fromJson(message, Args.class);
        StringData data = new StringData(request.getCellIndex(), request.getValue());
        String type = request.getType();

        switch (type) {
            case "set":
                try {
                    setData(data, output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "get":

                getData(data, output);

                break;
            case "delete":
                try {
                    deleteData(data, output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "exit":
                server.isWork = false;
                try {
                    output.writeUTF(gson.toJson(new DeleteOkResponse("OK")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException("ERROR");
        }
    }

    public void setData(Data data, DataOutputStream output) throws IOException {
        try {
            Data dataObject = checkDataAndReturnCorrectJsonObject(data);
            Response response = new Response(dataDaoIml.setDataToDb(dataObject));
            output.writeUTF(gson.toJson(response));
        } catch (Exception e) {
            ErrorResponse getErrorResponse = new ErrorResponse("ERROR", e.getMessage());
            output.writeUTF(gson.toJson(getErrorResponse));
        }

    }

    public void getData(Data data, DataOutputStream output) {
        try {
            String response = dataDaoIml.getDataFromDb(data.getKey());
            GetOkResponse getOkResponse = new GetOkResponse("OK", response);
            output.writeUTF(gson.toJson(getOkResponse));
        } catch (NullPointerException | IOException e) {
            ErrorResponse getErrorResponse = new ErrorResponse("ERROR", "No such key");
            try {
                output.writeUTF(gson.toJson(getErrorResponse));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void deleteData(Data data, DataOutputStream output) throws IOException {
        try {
            String response = dataDaoIml.deleteDataFromDb(data.getKey());
            DeleteOkResponse getOkResponse = new DeleteOkResponse(response);
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
        if (isBoolean) {
            return new BooleanData(data.getKey(), Boolean.parseBoolean(stringData.getValue()));
        } else if (isInteger) {
            return new IntegerData(data.getKey(), Integer.parseInt(stringData.getValue()));
        } else {
            return data;
        }
    }
}
