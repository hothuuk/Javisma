package data.schema.parser;

import data.schema.config.Field;
import data.schema.config.Model;

import java.util.ArrayList;
import java.util.List;

public class ModelParser {

    public Model parse(String blockContent) {
        Model model = new Model();
        List<Field> fields = new ArrayList<>();

        for (String line : blockContent.split("\n")) {
            String[] tokens = line.split(" ");
            String name = tokens[0];
            String type = tokens[1];

            fields.add(new Field(name, type));
        }

        model.setFields(fields);
        return model;
    }
}
