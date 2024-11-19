package data.schema;

import data.schema.config.Datasource;
import data.schema.config.Model;
import data.schema.parser.Block;
import data.schema.parser.DatasourceParser;
import data.schema.parser.ModelParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SchemaFileProcessor {

    private final LoadSchemaFiles loadSchemaFiles;
    private final DatasourceParser datasourceParser;
    private final ModelParser modelParser;

    public SchemaFileProcessor() {
        this.loadSchemaFiles = new LoadSchemaFiles();
        this.datasourceParser = new DatasourceParser();
        this.modelParser = new ModelParser();
    }

    public Datasource parseDatasource() {
        Block block = loadBlock("datasource");
        return datasourceParser.parse(block);
    }

    public Model parseModel() {
        Block block = loadBlock("model");
        return modelParser.parse(block);
    }

    private Block loadBlock(String blockName) {
        StringBuilder blockContent = new StringBuilder();
        String filePath = loadSchemaFiles.load().getPath();
        String name = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean foundBlock = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(blockName)) {
                    foundBlock = true;
                    name = line.split(" ")[1];
                    continue;
                }

                if (foundBlock) {
                    if (line.equals("}")) {
                        break;
                    }

                    blockContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Block(name, blockContent.toString());
    }
}
