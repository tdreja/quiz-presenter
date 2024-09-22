package de.dreja.quiz.service.persistence;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
@SuppressWarnings("unused")
public class SqlLiteSetup {

    @Bean
    @ConfigurationProperties("de.dreja.quiz.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    DataSource dataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    SpringLiquibase liquibase(DataSource dataSource) {
        final SpringLiquibase lb = new SpringLiquibase();
        lb.setChangeLog("classpath:db/changelog/master.yml");
        lb.setDataSource(dataSource);
        return lb;
    }
}
