package ru.yahoondex.archhelper.communicator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("communicator")
public class VaultConfigurationProperties {
    @Getter @Setter
    private String mailLogin;
    @Getter @Setter
    private String mailPassword;
    @Getter @Setter
    private String postgresUser;
    @Getter @Setter
    private String postgresPassword;
}