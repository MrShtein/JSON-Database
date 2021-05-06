package server;

import client.Data;

public class IntegerData extends Data {

    private Integer value;

    public IntegerData(String key, Integer value) {
        super(key);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
