package data.schema.parser;

import data.schema.config.Field;
import data.schema.config.Model;

import java.util.*;

public class ModelParser {

    // List of allowed types.
    private static final Set<String> VALID_TYPES = new HashSet<>(Arrays.asList("Int", "String"));

    public Model parse(Block block) {
        String blockContent = block.content();
        List<Field> fields = new ArrayList<>();
        int lineNumber = 0;

        for (String line : blockContent.split("\n")) {
            lineNumber++;
            String[] tokens = line.split(" ");

            if (tokens.length == 1) {
                if (VALID_TYPES.contains(tokens[0])) {
                    throw new IllegalStateException(
                            String.format("Field name is missing at line %d in the '%s' model.", lineNumber, block.name())
                    );
                }
            }

            String name = tokens[0];
            String type = tokens[1];

            fields.add(new Field(name, type));
        }

        return new Model(block.name(), fields);
    }
}
