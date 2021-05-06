package server;

import client.Data;

public class BooleanData extends Data {

    private boolean value;

    public BooleanData(String key, boolean value) {
        super(key);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }
}
