package server.Db;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.Data;
import server.BooleanData;
import server.IntegerData;
import server.StringData;

public class Db {

    String path = System.getProperty("user.dir") + "/src/server/data/db.json";
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();


    public synchronized String setValue(Data data) throws IllegalArgumentException {
        if (data.getClass() == BooleanData.class) {
            BooleanData booleanData = (BooleanData) data;
            writeLock.lock();
            try (FileOutputStream fos = new FileOutputStream(path);
                 OutputStreamWriter isr = new OutputStreamWriter(fos)) {

                Gson gson = new Gson();
                HashMap<String, Boolean> hm = new HashMap<>();
                hm.put(booleanData.getKey(), booleanData.isValue());
                gson.toJson(hm, isr);
                writeLock.unlock();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (data.getClass() == IntegerData.class) {
            IntegerData integerData = (IntegerData) data;
            writeLock.lock();
            try (FileOutputStream fos = new FileOutputStream(path);
                 OutputStreamWriter isr = new OutputStreamWriter(fos)) {
                Gson gson = new Gson();
                HashMap<String, Integer> hm = new HashMap<>();
                hm.put(integerData.getKey(), integerData.getValue());
                gson.toJson(hm, isr);
                writeLock.unlock();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            StringData stringData = (StringData) data;
            writeLock.lock();
            try (FileOutputStream fos = new FileOutputStream(path);
                 OutputStreamWriter isr = new OutputStreamWriter(fos)) {
                Gson gson = new Gson();
                HashMap<String, String> hm = new HashMap<>();
                hm.put(stringData.getKey(), stringData.getValue());
                System.out.println(hm);
                gson.toJson(hm, isr);
                writeLock.unlock();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return "OK";
    }

    public synchronized String getValue(String key) throws NullPointerException, IOException {
        readLock.lock();
        String jsonStr = Files.readString(Paths.get(path));
        readLock.unlock();
        if ("".equals(jsonStr)) {
            throw new NullPointerException("No such key");
        } else {
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            if (jsonObject == null) {
                throw new NullPointerException("No such key");
            }
            return jsonObject.get(key).getAsString();
        }
    }

    public synchronized String deleteValue(String key) throws Exception {
        try {
            readLock.lock();
            String jsonStr = Files.readString(Paths.get(path));
            readLock.unlock();
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            JsonElement je = jsonObject.remove(key);
            if (je == null) {
                throw new Exception("No such key");
            }
            writeLock.lock();
            try (FileOutputStream fos = new FileOutputStream(path);
                 OutputStreamWriter isr = new OutputStreamWriter(fos)) {
                Gson gson = new Gson();
                gson.toJson(jsonObject, isr);
                writeLock.unlock();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return "OK";
        } catch (Exception e) {
            throw new Exception("No such key");
        }
    }
}
