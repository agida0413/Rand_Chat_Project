package com.rand.config.aop;

import com.rand.config.rds.DataSourceContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Aspect
@Profile("deploy")
@Order(1)
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
