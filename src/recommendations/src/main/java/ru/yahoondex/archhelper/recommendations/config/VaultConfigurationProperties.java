package ru.yahoondex.archhelper.recommendations.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("recommendations")
public class VaultConfigurationProperties {
    @Getter @Setter
    private String yandexToken;
    @Getter @Setter
    private String yandexFolder;
    @Getter @Setter
    private String postgresUser;
    @Getter @Setter
    private String postgresPassword;
}
