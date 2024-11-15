package data.schema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchemaFileProcessor {

    private final LoadSchemaFiles loadSchemaFiles;

    public SchemaFileProcessor() {
        loadSchemaFiles = new LoadSchemaFiles();
    }

    public Map<String, String> parse(String blockName) {
        String filePath = loadSchemaFiles.load().getPath();
        return parseConfig(filePath, blockName);
    }

    private Map<String, String> parseConfig(String filePath, String blockName) {
        Map<String, String> config = new HashMap<>();
        String blockContent = "";

        try {
            blockContent = loadBlockContent(filePath, blockName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = blockContent.split("\n");

        for (String line : lines) {
            String[] tokens = line.split(" = ");
            String key = tokens[0];
            String value = tokens[1].replace("\"", "");
            config.put(key, value);
        }

        return config;
    }

    private String loadBlockContent(String filePath, String blockName) throws IOException {
        StringBuilder blockContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean foundBlock = false;

            while ((line = br.readLine()) != null) {

                if (line.startsWith(blockName)) {
                    foundBlock = true;
                    continue;
                }

                if (foundBlock) {
                    if (line.startsWith("}")) {
                        break;
                    }

                    blockContent.append(line.trim()).append("\n");
                }
            }
        }

        return blockContent.toString();
    }
}
