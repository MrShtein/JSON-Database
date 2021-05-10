package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import client.Data;
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

            StringData data = null;
            JsonObject request = JsonParser.parseString(message).getAsJsonObject();

            String type = request.get("type").getAsString();
            if ("exit".equals(type)) {
                server.isWork = false;
                try {
                    output.writeUTF(gson.toJson(new DeleteOkResponse("OK")));
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (hasArrayInKey(request)) {
                data = getKeyValueFromJsonArray(request);
                data.setHasArray(true);
            } else {
                String key = request.get("key").getAsString();
                JsonElement value = request.get("value");
                if (value == null) {
                    data = new StringData(key, "");
                } else {
                    data = new StringData(key, value.toString());
                }
                data.setHasArray(false);
            }

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
                default:
                    throw new IllegalArgumentException("ERROR");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            JsonElement response = dataDaoIml.getDataFromDb(data);
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
            String response = dataDaoIml.deleteDataFromDb(data);
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
        boolean isInteger = value.matches("[^\"]\\d+[^\"]");
        if (isBoolean) {
            return new BooleanData(data.getKey(), Boolean.parseBoolean(stringData.getValue()));
        } else if (isInteger) {
            return new IntegerData(data.getKey(), Integer.parseInt(stringData.getValue()));
        } else {
            return data;
        }
    }

    public boolean hasArrayInKey(JsonObject jo) {
        JsonElement je = jo.get("key");
        return je.isJsonArray();
    }

    public StringData getKeyValueFromJsonArray(JsonObject jo) {
        String key = jo.get("key").getAsJsonArray().toString();
        JsonElement value = jo.get("value");
        if (value == null) {
            return new StringData(key, "");
        } else {
            return new StringData(key, value.getAsString());
        }
    }
}
