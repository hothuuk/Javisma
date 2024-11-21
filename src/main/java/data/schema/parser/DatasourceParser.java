package data.schema.parser;

import data.schema.config.Datasource;

public class DatasourceParser {

    public Datasource parse(Block block) {
        String blockContent = block.content();

        String url = "";
        String user = "";
        String password = "";

        for (String line : blockContent.split("\n")) {
            String[] tokens = line.split(" = ");
            String key = tokens[0];
            String value = tokens[1].replace("\"", "");

            switch (key) {
                case "url" -> url = value;
                case "user" -> user = value;
                case "password" -> password = value;
            }
        }

        if (url.isBlank() || user.isBlank() || password.isBlank()) {
            throw new IllegalStateException("Datasource fields are missing.");
        }

        return new Datasource(url, user, password);
    }
}
