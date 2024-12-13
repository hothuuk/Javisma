package data.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class DriverLoaderTest {

    public static class MockDriver implements java.sql.Driver {
        @Override
        public Connection connect(String url, Properties info) { return null; }

        @Override
        public boolean acceptsURL(String url) { return false; }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) { return new DriverPropertyInfo[0]; }

        @Override
        public int getMajorVersion() { return 0; }

        @Override
        public int getMinorVersion() { return 0; }

        @Override
        public boolean jdbcCompliant() { return false; }

        @Override
        public Logger getParentLogger() { return null; }
    }

    @Test
    @DisplayName("Test: Should load driver successfully")
    public void should_load_driver_successfully() {
        // Given & When: Load MockDriver using DriverLoader
        assertDoesNotThrow(() -> DriverLoader.load("data.db.DriverLoaderTest$MockDriver"));
    }
}
