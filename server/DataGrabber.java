package server;

import java.util.Scanner;

import javax.security.sasl.SaslClient;

public class DataGrabber {

    private String method;
    private String num;
    private String value;

    public boolean handleData() {
        String data = new Scanner(System.in).nextLine();
        if ("exit".equals(data)) {
            return false;
        }
        String[] values = data.split(" ");
        setMethod(values[0]);
        setNum(values[1]);
        if (method.equals("set")) {

            setValue(getValueFromString(data));
        }
        return true;
    }

    public String getMethod() {
        return method;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getValueFromString(String data) {
        return data.replaceAll("((set)|(get)|(delete))\\s\\d*\\s", "");
    }
}
