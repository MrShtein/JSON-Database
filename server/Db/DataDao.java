package server.Db;

public interface DataDao {
    boolean setData(int index, String data);
    String getData(int index);
    boolean deleteData(int index);
}
