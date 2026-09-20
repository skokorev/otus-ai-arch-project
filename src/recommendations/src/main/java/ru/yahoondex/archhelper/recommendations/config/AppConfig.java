package ru.yahoondex.archhelper.recommendations.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "ru.yahoondex.archhelper.recommendations.config",
        "ru.yahoondex.archhelper.recommendations.services"
})
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:application.properties")
@EnableRetry
@EnableScheduling
public class AppConfig { }