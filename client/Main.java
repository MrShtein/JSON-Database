package client;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        client.start();
        client.join();
    }
}
