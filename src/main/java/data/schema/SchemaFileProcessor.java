package data.schema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchemaFileProcessor {

    private final LoadSchemaFiles loadSchemaFiles;

    public SchemaFileProcessor() {
        this.loadSchemaFiles = new LoadSchemaFiles();
    }

    public Map<String, String> parse(String blockName) {
        String filePath = loadSchemaFiles.load().getPath();

        try {
            String blockContent = loadBlockContent(filePath, blockName);
            return parseConfig(blockContent);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private Map<String, String> parseConfig(String blockContent) {
        Map<String, String> config = new HashMap<>();
        String[] lines = blockContent.split("\n");

        for (String line : lines) {
            String[] tokens = line.split(" = ");

            if (tokens.length == 2) {
                String key = tokens[0].trim();
                String value = tokens[1].replace("\"", "").trim();
                config.put(key, value);
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
