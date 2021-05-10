package server.Db;



import com.google.gson.JsonElement;

import java.io.IOException;

import client.Data;

public class DataDaoIml {
    Db db;

    public DataDaoIml() {
        this.db = new Db();
    }

    public String setDataToDb(Data jo) {
       return db.setValue(jo);
    }

    public JsonElement getDataFromDb(Data data) throws IOException {
        return db.getValue(data);
    }

    public String deleteDataFromDb(Data data) throws Exception {
        return db.deleteValue(data);
    }
}
