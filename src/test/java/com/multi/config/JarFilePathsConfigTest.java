package com.multi.config;

import com.multi.model.JarInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JarFilePathsConfigTest {
    @Test
    void getJarInfos() {
        JarFilePathsConfig yamlConfig = new JarFilePathsConfig("application.yml");
        List<JarInfo> jarInfos = yamlConfig.getJarInfos();

        assertEquals(2, jarInfos.size());
        assertEquals("C:\\workspace\\tx-auth-api\\target\\tx-auth-api-0.0.1-SNAPSHOT.jar", jarInfos.get(0).getPath());
        assertEquals("test", jarInfos.get(0).getProfile());
        assertEquals("C:\\workspace\\User\\tx-user-api\\target\\tx-user-api-0.0.1-SNAPSHOT.jar", jarInfos.get(1).getPath());
        assertEquals("test", jarInfos.get(1).getProfile());
    }
}
