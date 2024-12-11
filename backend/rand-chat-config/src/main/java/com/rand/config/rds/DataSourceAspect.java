package com.rand.config.rds;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;

@Aspect
@Profile("deploy")
public class DataSourceAspect {
    @Before("@annotation(readOnly)")
    public void useReadDataSource() {
        DataSourceContextHolder.setDataSourceType("READ");
    }

    @After("@annotation(readOnly)")
    public void clearDataSource() {
        DataSourceContextHolder.clearDataSourceType();
    }

    @Before("@annotation(writeOnly)")
    public void useWriteDataSource() {
        DataSourceContextHolder.setDataSourceType("WRITE");
    }
}
