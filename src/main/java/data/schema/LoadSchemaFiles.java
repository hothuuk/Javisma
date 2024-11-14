package data.schema;

import java.net.URL;

public class LoadSchemaFiles {

    private final static String DEFAULT_FILE_NAME = "/schema.javisma";

    public URL load() {
        return getURL(DEFAULT_FILE_NAME);
    }

    public URL load(String fileName) {
        return getURL(fileName);
    }

    private URL getURL(String fileName) {
        URL url = getClass().getResource(fileName);

        if (url == null) {
            throw new IllegalArgumentException("Schema file is not found.");
        }

        return url;
    }
}
