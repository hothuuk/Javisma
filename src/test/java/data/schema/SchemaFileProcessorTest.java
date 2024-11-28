package data.schema;

import data.schema.config.Datasource;
import data.schema.config.Field;
import data.schema.config.Model;
import data.schema.parser.DatasourceParser;
import data.schema.parser.ModelParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SchemaFileProcessorTest {

    private SchemaFileProcessor schemaFileProcessor;

    @BeforeEach
    public void setUp() {
        schemaFileProcessor = new SchemaFileProcessor();
    }

    @Test
    @DisplayName("데이터베이스 정보 파싱 테스트")
    public void schema_datasource_parsing() {
        // Given & When: 파싱 수행
        Datasource datasource = schemaFileProcessor.parseDatasource();

        // Then: 값 검증
        assertAll(
                () -> assertEquals("DATABASE_URL", datasource.url(), "url 이 맞지 않습니다."),
                () -> assertEquals("DATABASE_USER", datasource.user(), "user 가 맞지 않습니다."),
                () -> assertEquals("DATABASE_PASSWORD", datasource.password(), "password 가 맞지 않습니다.")
        );
    }

    @Test
    @DisplayName("모델 정보 파싱 테스트")
    public void schema_model_parsing() {
        // Given & When: 파싱 수행
        List<Model> models = schemaFileProcessor.parseModels();

        // Then: 모델과 필드 검증
        // 첫 번째 모델 검증
        Model firstModel = models.get(0);
        List<Field> firstFields = firstModel.fields();
        assertEquals("User", firstModel.name(), "모델 이름이 맞지 않습니다.");
        assertAll("첫 번째 모델 필드 검증",
                () -> assertEquals(3, firstFields.size(), "필드 개수가 맞지 않습니다."),
                () -> assertField(firstFields.get(0), "id", "Int"),
                () -> assertField(firstFields.get(1), "email", "String"),
                () -> assertField(firstFields.get(2), "name", "String")
        );

        // 두 번째 모델 검증
        Model secondModel = models.get(1);
        List<Field> secondFields = secondModel.fields();
        assertEquals("Post", secondModel.name(), "모델 이름이 맞지 않습니다.");
        assertAll("두 번째 모델 필드 검증",
                () -> assertEquals(2, secondFields.size(), "필드 개수가 맞지 않습니다."),
                () -> assertField(secondFields.get(0), "title", "String"),
                () -> assertField(secondFields.get(1), "content", "String")
        );
    }

    private void assertField(Field field, String expectedName, String expectedType) {
        assertAll(
                () -> assertEquals(expectedName, field.name(), "필드 이름이 맞지 않습니다."),
                () -> assertEquals(expectedType, field.type(), "필드 타입이 맞지 않습니다.")
        );
    }

    @Test
    @DisplayName("Test if block is not open")
    public void throw_exception_if_block_is_not_open() {
        // Given: Prepare a schema file for a block that is not open
        String mockSchema = """
                datasource db
                    url = "url"
                    user = "user"
                    password = "password"
                }
                """;

        SchemaFileProcessor mockSchemaFileProcessor = createMockSchemaFileProcessor(mockSchema);

        // When & Then: Expect an exception due to block is not open
        Exception exception = assertThrows(IllegalStateException.class, mockSchemaFileProcessor::parseDatasource);
        assertTrue(exception.getMessage().contains("The block is not properly opened. Expected '{' at the end of the line."),
                "Exception message is incorrect.");
    }

    @Test
    @DisplayName("블럭이 닫히지 않을 경우 테스트")
    public void block_is_not_closed() {
        // Given: 닫히지 않은 블럭을 포함한 스키마 파일 준비
        String mockSchema = """
                datasource db {
                    url = "url"
                    user = "user"
                    password = "password"
                
                model User {
                    id Int
                }
                """;

        SchemaFileProcessor mockSchemaFileProcessor = createMockSchemaFileProcessor(mockSchema);

        // When & Then: 블럭이 닫히지 않음으로 인해 Exception 발생
        Exception exception = assertThrows(IllegalStateException.class, mockSchemaFileProcessor::parseDatasource);
        assertTrue(exception.getMessage().contains("블럭이 닫히지 않았습니다."), "예외 메시지가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("Test for datasource not found")
    public void datasource_not_found() {
        // Given: Prepare a schema file that does not contain a datasource
        String mockSchema = """
                model User {
                    id Int
                    email String
                    name String
                }
                """;

        SchemaFileProcessor mockSchemaFileProcessor = createMockSchemaFileProcessor(mockSchema);

        // When & Then: Expect an exception due to missing datasource block
        Exception exception = assertThrows(IllegalStateException.class, mockSchemaFileProcessor::parseDatasource);
        assertTrue(exception.getMessage().contains("Datasource block is missing."), "Exception message is incorrect.");
    }

    @Test
    @DisplayName("Test for missing middle name")
    public void missing_middle_name() {
        // Given: Prepare a schema file that missing middle name
        String mockSchema = """
                datasource {
                    //...
                }
                
                model {
                    //...
                }
                """; // Missing middle name blocks

        SchemaFileProcessor mockSchemaFileProcessor = createMockSchemaFileProcessor(mockSchema);

        // When & Then: Expect an exceptions for missing middle name in datasource and model
        Exception datasourceException = assertThrows(
                IllegalStateException.class,
                mockSchemaFileProcessor::parseDatasource,
                "Expected exception when parsing datasource."
        );

        Exception modelException = assertThrows(
                IllegalStateException.class,
                mockSchemaFileProcessor::parseModels,
                "Expected exception when parsing models."
        );

        // Assert: Verify exception messages
        assertAll("Verify exception messages for missing middle names",
                () -> assertTrue(
                        datasourceException.getMessage().contains("datasource's middle name is missing"),
                        "Datasource exception message is incorrect."
                ),
                () -> assertTrue(
                        modelException.getMessage().contains("model's middle name is missing"),
                        "Model exception message is incorrect."
                )
        );
    }

    private SchemaFileProcessor createMockSchemaFileProcessor(String mockSchema) {
        try {
            File tempFile = File.createTempFile("schema", ".javisma");
            tempFile.deleteOnExit();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(mockSchema);
            }

            LoadSchemaFiles mockLoadSchemaFiles = mock(LoadSchemaFiles.class);
            when(mockLoadSchemaFiles.load()).thenReturn(tempFile.toURI().toURL());

            return new SchemaFileProcessor(mockLoadSchemaFiles, new DatasourceParser(), new ModelParser());
        } catch (IOException e) {
            throw new RuntimeException("Error creating mock schema processor", e);
        }
    }
}
