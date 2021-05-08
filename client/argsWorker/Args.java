package client.argsWorker;

import com.beust.jcommander.Parameter;
import com.google.gson.annotations.Expose;


public class Args {
    @Parameter(names = "-t", description = "-t is the type of the request")
    @Expose
    private String type;

    @Parameter(names = "-k", description = "-k is the key")
    @Expose
    private String key;

    @Parameter(names = "-v", description = " -v is the value to save in the database")
    @Expose
    private String value;

    @Parameter(names = "-in", description = "-in is information in file")
    private String path = "";

    public String getType() {
        return type;
    }

    public String getCellIndex() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }
}


