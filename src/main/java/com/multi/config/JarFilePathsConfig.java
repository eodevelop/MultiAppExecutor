package com.multi.config;

import com.multi.model.JarInfo;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JarFilePathsConfig {
    private final List<JarInfo> jarInfos = new ArrayList<>();

    public JarFilePathsConfig(String yamlFilePath) {
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(yamlFilePath);
        Map<String, List<Map<String, String>>> yamlData = yaml.load(inputStream);

        List<Map<String, String>> jarData = yamlData.get("jars");

        for (Map<String, String> data : jarData) {
            JarInfo jarInfo = new JarInfo(data.get("path"), data.get("profile"));
            jarInfos.add(jarInfo);
        }
    }

    public List<JarInfo> getJarInfos() {
        return jarInfos;
    }
}
