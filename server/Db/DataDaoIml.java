package server.Db;

public class DataDaoIml {
    Db db;

    public DataDaoIml() {
        this.db = new Db();
    }

    public String setDataToDb(int index, String value) {
        return db.setValue(index, value);
    }

    public String getDataFromDb(int index) {
        try {
            return db.getValue(index);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteDataFromDb(int index) {
        return db.deleteValue(index);
    }
}
