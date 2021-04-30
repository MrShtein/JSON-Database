package server;

public class MessageHandler {

    private String message;

    public MessageHandler(String message) {
        this.message = message;
    }

    public DataFromMessage handleMessage() {
        String[] data = message.split("\\+");
        DataFromMessage dataFromMessage = new DataFromMessage();
        if ("exit".equals(data[0])) {
            dataFromMessage.setCloseServer(true);
        }
        dataFromMessage.setAction(data[0]);
        if (data.length > 1) {
            dataFromMessage.setIndex(Integer.parseInt(data[1]));
        }
        if (data.length > 2) {
            dataFromMessage.setValue(data[2].trim());
        }
        return dataFromMessage;
    }
}
