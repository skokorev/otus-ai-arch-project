package ru.yahoondex.archhelper.recommendations.kafkaports;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.ArticleDto;
import ru.yahoondex.archhelper.recommendations.repositories.ArticleRepository;
import ru.yahoondex.archhelper.recommendations.repositories.ArticleSetRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.Article;
import ru.yahoondex.archhelper.recommendations.repositories.dao.ArticleSet;

import java.util.Arrays;

@Component
@Slf4j
public class ArticleListener {
    private final ArticleRepository articleRepository;
    private final ArticleSetRepository articleSetRepository;
    public ArticleListener(ArticleRepository articleRepository, ArticleSetRepository articleSetRepository) {
        this.articleRepository = articleRepository;
        this.articleSetRepository = articleSetRepository;
    }

    @KafkaListener(topics = {"article-topic"},
            groupId = "recommendations",
            containerFactory = "articleConcurrentKafkaListenerContainerFactory"
    )
    @Transactional
    public void listen(ArticleDto articleDto) {
        if (articleDto == null) {
            log.debug("Article is null");
            return;
        }
        articleRepository.save(new Article(articleDto.getId(), articleDto.getPublished(), articleDto.getAbstr(), articleDto.getTitle()));
        if (articleDto.getSets() == null || articleDto.getSets().length == 0) {
            log.debug("Set is empty");
        }
        Arrays.stream(articleDto.getSets()).forEach(setId -> articleSetRepository.save(new ArticleSet(articleDto.getId(), setId)));
    }
}
