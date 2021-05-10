package client;

public class Data {

    private String key;
    private boolean hasArray;

    public Data(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isHasArray() {
        return hasArray;
    }

    public void setHasArray(boolean hasArray) {
        this.hasArray = hasArray;
    }
}
