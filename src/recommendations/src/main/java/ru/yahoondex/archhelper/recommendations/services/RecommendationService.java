package ru.yahoondex.archhelper.recommendations.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.RecommendationDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.RecommendationListDto;
import ru.yahoondex.archhelper.recommendations.ai.tools.RecommendationItem;
import ru.yahoondex.archhelper.recommendations.repositories.GroupSetRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.GroupSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RecommendationService {
    private static final String userPromptTemplate = """
            "Сгенерируй рекомендации для группы {group}."
            {format}
            """;
    private final GroupSetRepository groupSetRepository;
    private final ChatClient chatClient;
    private final KafkaTemplate<String, RecommendationListDto> recommendationsKafkaTemplate;
    @Autowired
    public RecommendationService(
            GroupSetRepository groupSetRepository,
            ChatClient chatClient,
            KafkaTemplate<String, RecommendationListDto> recommendationsKafkaTemplate
    ) {
        this.groupSetRepository = groupSetRepository;
        this.chatClient = chatClient;
        this.recommendationsKafkaTemplate = recommendationsKafkaTemplate;
    }

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void generateRecommendations() {
        final StTemplateRenderer renderer = StTemplateRenderer.builder().build();
        final List<GroupSet> groupSets = groupSetRepository.findAll();
        final List<String> groupIds = groupSets.stream()
                .map(GroupSet::getGroupId)
                .distinct()
                .collect(Collectors.toList());
        groupIds.forEach(groupId -> {
            BeanOutputConverter<List<RecommendationItem>> converter = new BeanOutputConverter<>(
                    new ParameterizedTypeReference<>() {}
            );
            final String format = converter.getFormat();
            final PromptTemplate template = PromptTemplate.builder().template(userPromptTemplate).variables(Map.of("group", groupId,
                    "format", format)).build();
            final String userPrompt = template.render();

            final List<RecommendationItem> response = chatClient.prompt()
                    .user(userPrompt)
                    .call()
                    .entity(new ParameterizedTypeReference<>() {});


            Map<String, List<RecommendationItem>> setGroups = response.stream()
                    .collect(Collectors.groupingBy(RecommendationItem::setId));
            Map<String, List<RecommendationDto>> recommendations = new HashMap<>();
            setGroups.entrySet().forEach(entry -> {
                recommendations.put(entry.getKey(), entry.getValue().stream().map(i ->
                        new RecommendationDto(
                                i.articleId(),
                                i.title()))
                        .collect(Collectors.toList()));
            });
            RecommendationListDto recommendationListDto =
                    new RecommendationListDto(groupId, recommendations);
            recommendationsKafkaTemplate.send("recommendation-topic", groupId, recommendationListDto);
        });

    }
}
