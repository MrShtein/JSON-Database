package server.Db;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.Data;
import server.BooleanData;
import server.IntegerData;
import server.StringData;

public class Db {

    String path = System.getProperty("user.dir") + "/src/server/data/data.json";
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();


    public synchronized String setValue(Data data) throws IllegalArgumentException {
        try {
            writeLock.lock();
            JsonObject dataForDb = null;
            if (data.isHasArray()) {
                dataForDb = prepareDataForDb(data);
            } else {
                dataForDb = new JsonObject();
                dataForDb.add(data.getKey(), dataTypeCheckAndReturnKeyValueJson(data));
            }
            writeToFile(dataForDb);

        } catch (IOException e) {
            System.out.println("Db was broken");
        } finally {
            writeLock.unlock();
        }
        return "OK";
    }

    private void writeToFile(JsonObject dataForDb) {
        try {
            FileWriter fw = new FileWriter(path, false);
            String dataForWrite = dataForDb.toString();
            fw.write(dataForWrite);
            fw.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong Data");
        }
    }

    public synchronized JsonElement getValue(Data data) throws NullPointerException, IOException {
        readLock.lock();
        String jsonStr = Files.readString(Paths.get(path));
        readLock.unlock();
        if ("".equals(jsonStr)) {
            throw new NullPointerException("No such key");
        } else {
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            return findAndGetValue(data, jsonObject);
        }
    }

    public synchronized String deleteValue(Data data) throws Exception {
        try {
            readLock.lock();
            String jsonStr = Files.readString(Paths.get(path));
            readLock.unlock();
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            JsonElement updatedDb = removeValueAndGetResult(data, jsonObject);
            if (updatedDb == null) {
                throw new Exception("No such key");
            }
            writeLock.lock();
            try (FileOutputStream fos = new FileOutputStream(path);
                 OutputStreamWriter isr = new OutputStreamWriter(fos)) {
                Gson gson = new Gson();
                gson.toJson(updatedDb, isr);
                writeLock.unlock();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return "OK";
        } catch (Exception e) {
            throw new Exception("No such key");
        }
    }

    private JsonElement removeValueAndGetResult(Data data, JsonObject jsonObject) {
        if (data.isHasArray()) {
            Iterator<JsonElement> keys = JsonParser
                    .parseString(data.getKey()).getAsJsonArray().iterator();
            JsonElement currentElem = jsonObject;
            while (keys.hasNext()) {
                String currentKey = keys.next().getAsString();
                if (currentElem.getAsJsonObject().has(currentKey) && !keys.hasNext()) {
                    currentElem.getAsJsonObject().remove(currentKey);
                } else if (currentElem.getAsJsonObject().has(currentKey) && keys.hasNext()) {
                    currentElem = currentElem.getAsJsonObject().get(currentKey);
                } else {
                    throw new NullPointerException("No such key");
                }
            }
        } else {
            jsonObject.remove(data.getKey());
        }
        return jsonObject;
    }

    public JsonObject getJsonFromFile() throws IOException {
        String file = Files.readString(Paths.get(path));
        if ("".equals(file)) {
            return new JsonObject();
        }
        return JsonParser.parseString(Files.readString(Paths.get(path))).getAsJsonObject();
    }

    public JsonObject prepareDataForDb(Data data) throws IOException {
        JsonObject db = getJsonFromFile();
        return makeDataForDbFromArray(data, db);
    }

    public JsonObject makeDataForDbFromArray(Data data, JsonObject db) {
        if (data.getClass() == BooleanData.class) {
            return dataTypeCheckAndReturnJsonObject(data, "bool", db);
        } else if (data.getClass() == IntegerData.class) {
            return dataTypeCheckAndReturnJsonObject(data, "int", db);
        } else {
            return dataTypeCheckAndReturnJsonObject(data, "", db);
        }
    }

    public JsonObject dataTypeCheckAndReturnJsonObject(Data data, String type, JsonObject db) {
        Iterator<JsonElement> arr = JsonParser.parseString(data.getKey()).getAsJsonArray().iterator();
        JsonObject current = db;
        JsonObject newElem = null;
        while (arr.hasNext()) {
            String currentKey = arr.next().getAsString();
            if (!current.has(currentKey)) {
                if (arr.hasNext()) {
                    newElem = new JsonObject();
                    current.add(currentKey, newElem);
                    current = newElem;
                } else {
                    setSimpleValueToDb(data, type, current, currentKey);
                }
            } else {
                if (arr.hasNext()) {
                    current = current.get(currentKey).getAsJsonObject();
                } else {
                    setSimpleValueToDb(data, type, current, currentKey);
                }

            }
        }
        return db;
    }

    private void setSimpleValueToDb(Data data, String type, JsonObject current, String currentKey) {
        if ("bool".equals(type)) {
            BooleanData boolData = (BooleanData) data;
            current.addProperty(currentKey, boolData.isValue());
        } else if ("int".equals(type)) {
            IntegerData intData = (IntegerData) data;
            current.addProperty(currentKey, intData.getValue());
        } else {
            StringData strData = (StringData) data;
            current.addProperty(currentKey, strData.getValue());
        }
    }

    public JsonElement dataTypeCheckAndReturnKeyValueJson(Data data) {
        if (data.getClass() == BooleanData.class) {
            BooleanData boolData = (BooleanData) data;
            return new JsonPrimitive(boolData.isValue());
        } else if (data.getClass() == IntegerData.class) {
            IntegerData intData = (IntegerData) data;
            return new JsonPrimitive(intData.getValue());
        } else {
            StringData strData = (StringData) data;
            JsonElement temp = JsonParser.parseString(strData.getValue());
            if (temp.isJsonObject()) {
                return temp;
            }
            return new JsonPrimitive(strData.getValue());
        }
    }

    private JsonElement findAndGetValue(Data data, JsonObject db) {
        if (data.isHasArray()) {
            Iterator<JsonElement> jsonElementIterator = JsonParser
                    .parseString(data.getKey()).getAsJsonArray().iterator();
            JsonElement currentElem = db;
            while (jsonElementIterator.hasNext()) {
                String currentKey = jsonElementIterator.next().getAsString();
                currentElem = currentElem.getAsJsonObject().get(currentKey);
                if (currentElem == null) {
                    throw new NullPointerException("No such key");
                }
            }
            return currentElem;
        } else {
            JsonElement simpleElement = db.get(data.getKey());
            if (simpleElement == null) {
                throw new NullPointerException("No such key");
            } else {
                return simpleElement;
            }
        }
    }
}




