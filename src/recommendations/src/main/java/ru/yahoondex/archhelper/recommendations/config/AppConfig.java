package ru.yahoondex.archhelper.recommendations.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = {"ru.yahoondex.archhelper.recommendations.config"})
@PropertySource("classpath:application.properties")
@EnableRetry
public class AppConfig { }