package client.argsWorker;

import com.beust.jcommander.Parameter;


public class Args {
    @Parameter(names = "-t", description = "-t is the type of the request")
    private String type;

    @Parameter(names = "-k", description = "-k is the key")
    private String key;

    @Parameter(names = "-v", description = " -v is the value to save in the database")
    private String value;

    public String getType() {
        return type;
    }

    public String getCellIndex() {
        return key;
    }

    public String getValue() {
        return value;
    }
}


