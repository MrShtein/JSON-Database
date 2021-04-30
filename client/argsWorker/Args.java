package client.argsWorker;

import com.beust.jcommander.IDefaultProvider;
import com.beust.jcommander.Parameter;


public class Args {
    @Parameter(names = "-t", description = "-t is the type of the request")
    private String type = "";

    @Parameter(names = "-i", description = "-i is the index of the cell")
    private Integer cellIndex;

    @Parameter(names = "-m", description = " -m is the value to save in the database")
    private String value;

    @Override
    public String toString() {
        String tempType = type == null ? "" : type;
        String tempCellIndex = cellIndex == null ? "" : String.valueOf(cellIndex);
        String tempValue = value == null ? "" : value;
        return tempType + "+" + tempCellIndex + "+" + tempValue;
    }
}


