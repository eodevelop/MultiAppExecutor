package com.multi.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JarFilePathsConfigTest {
    @Test
    void getJarFilePaths() {
        JarFilePathsConfig yamlConfig = new JarFilePathsConfig("application.yml");
        List<String> jarFilePaths = yamlConfig.getJarFilePaths();

        assertNotNull(jarFilePaths);
        assertEquals(2, jarFilePaths.size());
    }
}
