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
        assertTrue(exception.getMessage().contains("Datasource fields are missing: driver password"), "Exception message is incorrect.");
    }

    @Test
    @DisplayName("Test for missing equal signs in content")
    public void missing_equal_signs() {
        // Given: Prepare a block content where missing equal signs
        String mockContent = """
                url "url"
                user "user"
                password "password"
                """; // Missing equal signs

        Block block = new Block("db", mockContent);

        // When & Then: Expect an exception due to missing equal signs
        Exception exception = assertThrows(IllegalStateException.class, () -> datasourceParser.parse(block));
        assertTrue(exception.getMessage().contains("Equal signs are missing on datasource content"), "Exception message is incorrect.");
    }
}
