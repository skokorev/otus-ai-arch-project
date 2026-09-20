package ru.yahoondex.archhelper.recommendations.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.yahoondex.archhelper.recommendations.ai.tools.CustomTools;

import java.nio.charset.StandardCharsets;

@Configuration
@ComponentScan(basePackages = {"ru.yahoondex.archhelper.recommendations.ai.tools"})
@EnableConfigurationProperties(VaultConfigurationProperties.class)
public class AIConfig { 
    @Autowired
    private CustomTools customTools;
    @Value("#{T(java.nio.file.Files).readString(T(java.nio.file.Path).of('/system-prompt.md'), T(java.nio.charset.StandardCharsets).UTF_8)}")
    private String systemPrompt;

    @Autowired
    private VaultConfigurationProperties vaultConfigurationProperties;

    @Bean
    ChatModel chatModel(
        @Value("${spring.ai.openai.base-url}") String chatModelBaseUrl,
        @Value("${spring.ai.openai.chat.model}") String modelName,
        @Value("${spring.ai.openai.chat.temperature}") Double temperature
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
    public ChatClient chatClient(ChatModel model) {
        return ChatClient.builder(model)
                .defaultTools(customTools)
                .defaultSystem(systemPrompt)
                .build();
    }
}