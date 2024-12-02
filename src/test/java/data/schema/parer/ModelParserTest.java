package data.schema.parer;

import data.schema.parser.Block;
import data.schema.parser.ModelParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelParserTest {

    private ModelParser modelParser;

    @BeforeEach
    public void setUp() {
        modelParser = new ModelParser();
    }

    @Test
    @DisplayName("Test: If field name is missing.")
    public void throw_exception_if_field_name_is_missing() {
        // Given: Prepare block content with fields with missing names.
        String mockContent = """
                id Int
                String
                """;

        Block block = new Block("User", mockContent);

        // When & Then: Expect an exception due to name of the field is missing.
        Exception exception = assertThrows(IllegalStateException.class, () -> modelParser.parse(block));
        assertTrue(exception.getMessage().contains("Field name is missing at line 2 in the 'User' model."), "Exception message is incorrect.");
    }
}
