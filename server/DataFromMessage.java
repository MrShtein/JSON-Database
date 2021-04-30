package server;

public class DataFromMessage {

    private String action;
    private int index;
    private String value;
    private boolean closeServer;

    public String getAction() {
        return action;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public boolean getCloseServer() {
        return closeServer;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setCloseServer(boolean closeServer) {
        this.closeServer = closeServer;
    }
}
