package ru.yahoondex.archhelper.communicator.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "ru.yahoondex.archhelper.communicator.config"
})
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:application.properties")
public class AppConfig { }
