package ru.yahoondex.archhelper.crawler.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "ru.yahoondex.archhelper.crawler.config",
        "ru.yahoondex.archhelper.crawler.services"
})
@EnableTransactionManagement(proxyTargetClass = true)
@EnableScheduling
@PropertySource("classpath:application.properties")
public class AppConfig { }