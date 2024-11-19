package data.schema;

import data.schema.config.Datasource;
import data.schema.config.Model;
import data.schema.parser.Block;
import data.schema.parser.DatasourceParser;
import data.schema.parser.ModelParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        Block block = loadBlocks("datasource").get(0);
        return datasourceParser.parse(block);
    }

    public List<Model> parseModels() {
        List<Block> blocks = loadBlocks("model");
        List<Model> models = new ArrayList<>();

        for (Block block : blocks) {
            models.add(modelParser.parse(block));
        }

        return models;
    }

    private List<Block> loadBlocks(String blockName) {
        List<Block> blocks = new ArrayList<>();
        String filePath = loadSchemaFiles.load().getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean foundBlock = false;
            StringBuilder blockContent = new StringBuilder();
            String name = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(blockName)) {
                    if (foundBlock) {
                        blocks.add(new Block(name, blockContent.toString()));
                    }

                    foundBlock = true;
                    name = line.split(" ")[1];
                    blockContent = new StringBuilder();
                    continue;
                }

                if (foundBlock) {
                    if (line.equals("}")) {
                        blocks.add(new Block(name, blockContent.toString()));
                        foundBlock = false;
                    } else {
                        blockContent.append(line).append("\n");
                    }
                }
            }

            if (foundBlock) {
                blocks.add(new Block(name, blockContent.toString()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return blocks;
    }
}
