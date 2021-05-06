package server.Db;



import client.Data;

public class DataDaoIml {
    Db db;

    public DataDaoIml() {
        this.db = new Db();
    }

    public String setDataToDb(Data jo) {
       return db.setValue(jo);
    }

    public String getDataFromDb(String key) {
        return db.getValue(key);
    }

    public String deleteDataFromDb(String key) throws Exception {
        return db.deleteValue(key);
    }
}
