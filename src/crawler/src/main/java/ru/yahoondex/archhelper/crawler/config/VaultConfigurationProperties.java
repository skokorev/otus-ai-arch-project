package ru.yahoondex.archhelper.crawler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("crawler")
public class VaultConfigurationProperties {
    @Getter @Setter
    private String postgresUser;
    @Getter @Setter
    private String postgresPassword;
}
