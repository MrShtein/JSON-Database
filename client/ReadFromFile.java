package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFromFile {

    private String path;

    public ReadFromFile(String path) {
        this.path = path;
    }

    public String readFile() throws IOException {
        return Files.readString(Path.of(path));
    }

    public String getPath() {
        return path;
    }
}
