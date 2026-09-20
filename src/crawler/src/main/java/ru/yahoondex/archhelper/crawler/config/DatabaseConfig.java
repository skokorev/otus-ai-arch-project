package ru.yahoondex.archhelper.crawler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("ru.yahoondex.archhelper.crawler.repositories")
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class DatabaseConfig {
    @Value("${datasource.url}")
    private String databaseUrl;

    @Autowired
    private VaultConfigurationProperties vaultConfigurationProperties;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(databaseUrl);
        dataSourceBuilder.username(vaultConfigurationProperties.getPostgresUser());
        dataSourceBuilder.password(vaultConfigurationProperties.getPostgresPassword());
        return dataSourceBuilder.build();
    }
}