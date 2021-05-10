package client;

import com.beust.jcommander.ParameterException;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws ParameterException, InterruptedException {
        Client client = new Client(args);
        client.start();
        client.join();

    }
}
