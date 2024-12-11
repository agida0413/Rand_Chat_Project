package com.rand.config.rds;

import org.springframework.context.annotation.Profile;

@Profile("deploy")
public class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setDataSourceType(String dataSourceType) {
        CONTEXT.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return CONTEXT.get();
    }

    public static void clearDataSourceType() {
        CONTEXT.remove();
    }
}