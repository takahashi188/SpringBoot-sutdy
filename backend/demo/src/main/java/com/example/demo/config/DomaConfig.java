package com.example.demo.config;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomaConfig {

    @Bean
    public Config domaJdbcConfig(DataSource dataSource) {

        return new Config() {

            @Override
            public DataSource getDataSource() {
                return dataSource;
            }

            @Override
            public Dialect getDialect() {
                return new PostgresDialect();
            }

            @Override
            public JdbcLogger getJdbcLogger() {
                return new UtilLoggingJdbcLogger();
            }
        };
    }
}