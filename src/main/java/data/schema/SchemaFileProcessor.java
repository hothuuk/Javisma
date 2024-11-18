package data.schema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SchemaFileProcessor {

    private final LoadSchemaFiles loadSchemaFiles;

    public SchemaFileProcessor() {
        this.loadSchemaFiles = new LoadSchemaFiles();
    }

    public Map<String, String> parseDatasource(String blockName) {
        return parse(blockName, this::parseConfig);
    }

    public Map<String, String> parseModel(String blockName) {
        return parse(blockName, this::parseModelConfig);
    }

    private Map<String, String> parse(String blockName, Function<String, Map<String, String>> parser) {
        String filePath = loadSchemaFiles.load().getPath();

        try {
            String blockContent = loadBlockContent(filePath, blockName);
            return parser.apply(blockContent);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private Map<String, String> parseConfig(String blockContent) {
        Map<String, String> config = new HashMap<>();

        for (String line : blockContent.split("\n")) {
            String[] tokens = line.split(" = ");

            if (tokens.length == 2) {
                config.put(tokens[0], tokens[1].replace("\"", ""));
            }
        }

        return config;
    }

    private Map<String, String> parseModelConfig(String blockContent) {
        Map<String, String> config = new HashMap<>();

        for (String line : blockContent.split("\n")) {
            String[] tokens = line.split(" ");

            if (tokens.length == 2) {
                config.put(tokens[0], tokens[1]);
            }
        }

        return config;
    }

    private String loadBlockContent(String filePath, String blockName) throws IOException {
        StringBuilder blockContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean foundBlock = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(blockName)) {
                    foundBlock = true;
                    continue;
                }

                if (foundBlock) {
                    if (line.equals("}")) {
                        break;
                    }
                    blockContent.append(line).append("\n");
                }
            }
        }

        return blockContent.toString();
    }
}
