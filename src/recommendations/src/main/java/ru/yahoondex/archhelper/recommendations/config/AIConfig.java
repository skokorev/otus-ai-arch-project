package ru.yahoondex.archhelper.recommendations.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig { 

//    @Bean
//    ChatModel chatModel(
//        @Value("${spring.ai.openai.base-url}") String chatModelBaseUrl,
//        @Value("${spring.ai.openai.api-key}") String apiKey,
//        @Value("${spring.ai.openai.chat.options.model}") String modelName,
//        @Value("${yandex.ai.model.folder}") String folder,
//        @Value("${spring.ai.openai.chat.temperature}") Double temperature
//    ) {
//        final String gptModelUrl = String.format("gpt://%s/%s", folder, modelName);
//        return OpenAiChatModel.builder()
//                .options(OpenAiChatOptions.builder()
//                    .baseUrl(chatModelBaseUrl)
//                    .apiKey(apiKey)
//                    .model(gptModelUrl)
//                    .temperature(temperature)
//                    .build())
//                .build();
//    }
//
//    @Bean
//    public ChatClient chatClient(ChatModel model) {
//        return ChatClient.builder(model)
//        .build();
//    }
}