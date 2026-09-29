package ru.yahoondex.archhelper.recommendations.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import ru.yahoondex.archhelper.recommendations.ai.tools.CustomTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Configuration
@ComponentScan(basePackages = {"ru.yahoondex.archhelper.recommendations.ai.tools"})
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class AIConfig { 
    @Autowired
    private CustomTools customTools;

    @Value("classpath:system-prompt.md")
    private Resource systemPromptFile;

    @Autowired
    private VaultConfigurationProperties vaultConfigurationProperties;

    @Bean
    ChatModel yandexChatModel(
        @Value("${recommendation.ai.openai.base-url}") String chatModelBaseUrl,
        @Value("${recommendation.ai.openai.chat.model}") String modelName,
        @Value("${recommendation.ai.openai.chat.temperature}") Double temperature
    ) {
        final String gptModelUrl = String.format("gpt://%s/%s", vaultConfigurationProperties.getYandexFolder(), modelName);
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                    .baseUrl(chatModelBaseUrl)
                    .apiKey(vaultConfigurationProperties.getYandexToken())
                    .model(gptModelUrl)
                    .temperature(temperature)
                    .build())
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatModel yandexChatModel) throws IOException {
        try (InputStream is = systemPromptFile.getInputStream();
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            final String systemPrompt = FileCopyUtils.copyToString(reader);
            return ChatClient.builder(yandexChatModel)
                    .defaultTools(customTools)
                    .defaultSystem(systemPrompt)
                    .build();
        }
    }
}