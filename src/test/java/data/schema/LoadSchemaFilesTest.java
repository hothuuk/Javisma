package data.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class LoadSchemaFilesTest {

    private LoadSchemaFiles loadSchemaFiles;

    @BeforeEach
    public void setUp() {
        loadSchemaFiles = new LoadSchemaFiles();
    }

    @Test
    @DisplayName("스키마 파일 불러오기 테스트")
    public void load_schema_files() {
        // Given & When
        URL url = loadSchemaFiles.load();

        // Then
        assertNotNull(url);
    }

    @Test
    @DisplayName("커스텀 경로에서 스키마 파일 불러오기 테스트")
    public void custom_file_directory() {
        // Given
        String customFile = "/custom/custom.schema.javisma";

        // When
        URL url = loadSchemaFiles.load(customFile);

        // Then
        assertNotNull(url);
    }

    @Test
    @DisplayName("경로에 스키마 파일이 없는 경우 테스트")
    public void file_not_found() {
        // Given
        String customFile = "/error.javisma";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                loadSchemaFiles.load(customFile)
        );

        assertEquals("Schema file is not found.", exception.getMessage());
    }
}
