package data.schema;

import java.net.URL;

public class LoadSchemaFiles {

    private final static String DEFAULT_FILE_NAME = "/schema.javisma";

    public URL load() {
        return getClass().getResource(DEFAULT_FILE_NAME);
    }
}
