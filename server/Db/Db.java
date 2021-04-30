package server.Db;

import java.util.HashMap;

public class Db {

    HashMap<Integer, String> values;

    public Db() {
        values = new HashMap<>();
        hashMapInit();
    }

    private void hashMapInit() {
        for (int i = 1; i <= 1000; i++) {
            values.put(i, "");
        }
    }

    public String setValue(int num, String value) throws IllegalArgumentException {
        if (checkNum(num)) {
            values.put(num, value);
            return "OK";
        }
        throw new IllegalArgumentException("ERROR");
    }

    public String getValue(int num) throws Exception {
        if (checkNum(num)) {
            String value = values.get(num);
            return checkValue(value);
        }
        throw new Exception("ERROR");
    }

    public boolean checkNum(int num) {
        return num >= 1 && num <= 100;
    }

    public String checkValue(String value) throws Exception {
        if ("".equals(value)) {
            throw new Exception("ERROR");
        }
        return value;
    }

    public String deleteValue(int num) {
        if (checkNum(num)) {
            values.put(num, "");
            return "OK";
        }
        return "ERROR";
    }

}
