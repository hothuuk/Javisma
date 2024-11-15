package data.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SchemaFileProcessorTest {

    private SchemaFileProcessor schemaFileProcessor;

    @BeforeEach
    public void setUp() {
        schemaFileProcessor = new SchemaFileProcessor();
    }

    @Test
    @DisplayName("데이터베이스 정보 파싱 테스트")
    public void schema_datasource_parsing() {
        // Given
        String blockName = "datasource";

        // When
        Map<String, String> config = schemaFileProcessor.parse(blockName);

        // Then
        assertEquals("DATABASE_URL", config.get("url"));
        assertEquals("DATABASE_USER", config.get("user"));
        assertEquals("DATABASE_PASSWORD", config.get("password"));
    }
}
