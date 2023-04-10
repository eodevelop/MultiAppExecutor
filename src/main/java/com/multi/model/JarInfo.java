package com.multi.model;

public class JarInfo {
    private String path;
    private String profile;

    public JarInfo(String path, String profile) {
        this.path = path;
        this.profile = profile;
    }

    public String getPath() {
        return path;
    }

    public String getProfile() {
        return profile;
    }
}
