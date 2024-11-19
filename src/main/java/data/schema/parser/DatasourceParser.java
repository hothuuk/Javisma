package data.schema.parser;

import data.schema.config.Datasource;

public class DatasourceParser {

    public Datasource parse(String blockContent) {
        Datasource datasource = new Datasource();

        for (String line : blockContent.split("\n")) {
            String[] tokens = line.split(" = ");
            String key = tokens[0];
            String value = tokens[1].replace("\"", "");

            switch (key) {
                case "url" -> datasource.setUrl(value);
                case "user" -> datasource.setUser(value);
                case "password" -> datasource.setPassword(value);
            }
        }

        return datasource;
    }
}
