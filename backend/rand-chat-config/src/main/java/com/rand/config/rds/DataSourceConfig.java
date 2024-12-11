package com.rand.config.rds;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("deploy")
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String writeDbUrl;

    @Value("${spring.datasource.username}")
    private String writeDbUsername;

    @Value("${spring.datasource.password}")
    private String writeDbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String writeDbDriverClassName;

    @Value("${spring.read-datasource.url}")
    private String readDbUrl;

    @Value("${spring.read-datasource.username}")
    private String readDbUsername;

    @Value("${spring.read-datasource.password}")
    private String readDbPassword;

    @Value("${spring.read-datasource.driver-class-name}")
    private String readDbDriverClassName;

    @Primary
    @Bean(name = "writeDataSource")
    public DataSource writeDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(writeDbUrl);
        dataSource.setUsername(writeDbUsername);
        dataSource.setPassword(writeDbPassword);
        dataSource.setDriverClassName(writeDbDriverClassName);
        return dataSource;
    }

    @Bean(name = "readDataSource")
    public DataSource readDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(readDbUrl);
        dataSource.setUsername(readDbUsername);
        dataSource.setPassword(readDbPassword);
        dataSource.setDriverClassName(readDbDriverClassName);
        return dataSource;
    }


    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    private AbstractRoutingDataSource routingDataSource() {
        CustomRoutingDataSource routingDataSource = new CustomRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("WRITE", writeDataSource());
        targetDataSources.put("READ", readDataSource());
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(writeDataSource());
        return routingDataSource;
    }
}

