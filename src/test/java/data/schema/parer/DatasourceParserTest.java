package data.schema.parer;

import data.schema.parser.Block;
import data.schema.parser.DatasourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatasourceParserTest {

    private DatasourceParser datasourceParser;

    @BeforeEach
    public void setUp() {
        datasourceParser = new DatasourceParser();
    }

    @Test
    @DisplayName("Test for missing datasource fields")
    public void datasource_fields_missing() {
        // Given: Prepare a block content where datasource fields are missing
        String mockContent = """
                url = "url"
                user = "user"
                """; // password is missing

        Block block = new Block("db", mockContent);

        // When & Then: Expect an exception due to missing fields
        Exception exception = assertThrows(IllegalStateException.class, () -> datasourceParser.parse(block));
        assertTrue(exception.getMessage().contains("Datasource fields are missing: password"), "Exception message is incorrect.");
    }
}
