package data.schema;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class LoadSchemaFilesTest {

    @Test
    @DisplayName("스키마 파일 불러오기 테스트")
    public void load_schema_files() {
        // Given
        LoadSchemaFiles loadSchemaFiles = new LoadSchemaFiles();

        // When
        URL url = loadSchemaFiles.load();

        // Then
        assertNotNull(url);
    }

    @Test
    @DisplayName("커스텀 경로에서 스키마 파일 불러오기 테스트")
    public void custom_file_directory() {
        // Given
        String customFile = "/custom/custom.schema.javisma";
        LoadSchemaFiles loadSchemaFiles = new LoadSchemaFiles();

        // When
        URL url = loadSchemaFiles.load(customFile);

        // Then
        assertNotNull(url);
    }
}
