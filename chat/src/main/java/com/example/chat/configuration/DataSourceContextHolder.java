package com.example.chat.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataSourceContextHolder {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setBranchContext(String tenantId) {
        threadLocal.set(tenantId);
    }

    public static String getBranchContext() {
        return threadLocal.get();
    }

    public static void clearBranchContext() {
        threadLocal.remove();
    }
}
