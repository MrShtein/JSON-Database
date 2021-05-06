package server;

import client.Data;

public class StringData extends Data {

    private String value;

    public StringData(String key, String value) {
        super(key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
