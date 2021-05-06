package server.Db;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jdi.IntegerValue;

import client.Data;
import server.BooleanData;
import server.IntegerData;
import server.StringData;

public class Db {

    JsonObject db;

    public Db() {
        this.db = new JsonObject();
    }

    public String setValue(Data data) throws IllegalArgumentException {
        if (data.getClass() == BooleanData.class) {
            BooleanData booleanData = (BooleanData) data;
            db.addProperty(booleanData.getKey(), booleanData.isValue());
        } else if (data.getClass() == IntegerData.class) {
            IntegerData integerData = (IntegerData) data;
            db.addProperty(integerData.getKey(), integerData.getValue());
        } else {
            StringData stringData = (StringData) data;
            db.addProperty(stringData.getKey(), stringData.getValue());
        }
        return "OK";
    }

    public String getValue(String key) {
        return db.get(key).getAsString();
    }

    public String deleteValue(String key) throws Exception {
        JsonElement response = db.remove(key);
        if (response == null) {
            throw new Exception("No such key");
        }
        return "OK";
    }



}
