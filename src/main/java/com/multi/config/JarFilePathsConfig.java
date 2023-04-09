package com.multi.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JarFilePathsConfig {
    private final Map<String, Object> yamlData;

    public JarFilePathsConfig(String yamlFilePath) {
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(yamlFilePath);
        yamlData = yaml.load(inputStream);
    }

    public List<String> getJarFilePaths() {
        return (List<String>) yamlData.get("jarFilePaths");
    }
}
