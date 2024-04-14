package com.edbrn.Campfires.files;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class CampfiresConfigTests {
    @AfterEach
    public void afterEach() {
        File configFile = new File("campfires-test.json");
        configFile.delete();
    }

    @Test
    public void testCreatesCampfiresConfigIfItDoesntExistAlready() {
        File configFile = new File("campfires-test.json");
        assertFalse(configFile.exists());

        CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), "campfires-test.json");
        config.getCampfires();

        assertTrue(configFile.exists());
    }

    @Test
    public void testPopulatesFileWithSingleKeyEmptyArray() {
        File configFile = new File("campfires-test.json");
        assertFalse(configFile.exists());

        CampfiresConfig config = new CampfiresConfig(Logger.getAnonymousLogger(), "campfires-test.json");
        config.getCampfires();

        try {
            String configContent = Files.readString(Path.of("campfires-test.json"));
            assertEquals(configContent, "{\"campfires\": []}");
        } catch (Exception e) {
            fail(e);
        }
    }
}
