package data.schema;

import data.schema.config.Datasource;
import data.schema.config.Field;
import data.schema.config.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Model model = schemaFileProcessor.parseModel();
        List<Field> fields = model.fields();

        // Then: 모델과 필드 검증
        assertEquals("User", model.name(), "모델 이름이 맞지 않습니다.");

        assertAll("필드 개수 및 값 검증",
                () -> assertEquals(3, fields.size(), "필드 개수가 맞지 않습니다."),
                () -> assertField(fields.get(0), "id", "Int"),
                () -> assertField(fields.get(1), "email", "String"),
                () -> assertField(fields.get(2), "name", "String")
        );
    }

    private void assertField(Field field, String expectedName, String expectedType) {
        assertAll(
                () -> assertEquals(expectedName, field.name(), "필드 이름이 맞지 않습니다."),
                () -> assertEquals(expectedType, field.type(), "필드 타입이 맞지 않습니다.")
        );
    }
}
