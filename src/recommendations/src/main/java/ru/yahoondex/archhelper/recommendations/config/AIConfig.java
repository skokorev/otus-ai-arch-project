package ru.yahoondex.archhelper.recommendations.config;

@Configuration
public class AIConfig { 

    @Bean ChatModel chatModel(
        @Value("${spring.ai.openai.base-url}") String chatModelBaseUrl,
        @Value("${spring.ai.openai.api-key}") String apiKey,
        @Value("${spring.ai.openai.chat.options.model}") String modelName,
        @Value("${yandex.ai.model.folder}") String folder,
        @Value("${spring.ai.openai.chat.temperature}") float temperature
    ) {
        final String gptModelUrl = String.format("gpt://%s/%s", folder, modelName);
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                    .baseUrl(chatModelBaseUrl)
                    .apiKey(apiKey)
                    .model(gptModelUrl)
                    .temperature(temperature)
                    .build())
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatModel model) {
        return ChatClient.builder(model)
        .build();
    }
}