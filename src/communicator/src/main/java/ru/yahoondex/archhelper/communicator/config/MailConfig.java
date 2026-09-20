package ru.yahoondex.archhelper.communicator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class MailConfig {

    @Autowired
    private VaultConfigurationProperties vaultConfigurationProperties;

    @Bean
    public JavaMailSender getMailSender(
            @Value("${communicator.mail.host}") String mailHost,
            @Value("${communicator.mail.port}") int mailPort,
            @Value("${communicator.mail.properties.mail.smtp.auth}") String smtpAuth,
            @Value("${communicator.mail.properties.mail.smtp.starttls.enable}") String startTls
    ) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(mailHost);
        sender.setPort(mailPort);
        sender.setUsername(vaultConfigurationProperties.getMailLogin());
        sender.setPassword(vaultConfigurationProperties.getMailPassword());
        sender.setDefaultEncoding("UTF-8");

        Properties properties = sender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", smtpAuth);
        properties.setProperty("mail.smtp.starttls.enable", startTls);

        return sender;
    }

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
