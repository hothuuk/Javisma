package data.schema.parser;

import data.schema.config.Datasource;

import java.util.HashMap;
import java.util.Map;

public class DatasourceParser {

    public Datasource parse(Block block) {
        String blockContent = block.content();
        Map<String, String> fields = new HashMap<>();

        for (String line : blockContent.split("\n")) {
            String[] tokens = line.split("=");

            if (tokens.length == 1) {
                throw new IllegalStateException("Equal signs are missing on datasource content");
            }

            if (tokens.length == 2) {
                fields.put(tokens[0].trim(), tokens[1].replace("\"", "").trim());
            }
        }

        if (!fields.containsKey("driver") ||
                !fields.containsKey("url") ||
                !fields.containsKey("user") ||
                !fields.containsKey("password")
        ) {
            throw new IllegalStateException("Datasource fields are missing: "
                    + (fields.containsKey("driver") ? "" : "driver ")
                    + (fields.containsKey("url") ? "" : "url ")
                    + (fields.containsKey("user") ? "" : "user ")
                    + (fields.containsKey("password") ? "" : "password"));
        }

        return new Datasource(fields.get("driver"), fields.get("url"), fields.get("user"), fields.get("password"));
    }
}
