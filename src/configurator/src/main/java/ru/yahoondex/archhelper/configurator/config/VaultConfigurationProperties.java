package ru.yahoondex.archhelper.configurator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("configurator")
public class VaultConfigurationProperties {
    @Getter @Setter
    private String postgresUser;
    @Getter @Setter
    private String postgresPassword;
    @Getter @Setter
    private String clientSecret;
}
