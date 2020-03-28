package com.example.lib_api;

public enum ApiMode {
    REAL,
    STAGE,
    TEST,
    DEV;

    public static ApiMode get(String buildType) {
        try {
            return ApiMode.valueOf(buildType.toUpperCase());
        }catch (Exception e) {
            return REAL;
        }
    }
}
