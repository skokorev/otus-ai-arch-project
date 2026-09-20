package ru.yahoondex.archhelper.configurator.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "ru.yahoondex.archhelper.configurator.config",
        "ru.yahoondex.archhelper.configurator.services"
})
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:application.properties")
public class AppConfig {
}
