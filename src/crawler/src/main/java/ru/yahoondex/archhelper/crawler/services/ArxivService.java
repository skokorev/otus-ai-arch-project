package ru.yahoondex.archhelper.crawler.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.commons.contracts.kafka.ArticleDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.NameSetDto;
import ru.yahoondex.archhelper.commons.contracts.kafka.Operation;
import ru.yahoondex.archhelper.crawler.arxivports.ArxivClient;
import ru.yahoondex.archhelper.crawler.repositories.GroupSetRepository;
import ru.yahoondex.archhelper.crawler.repositories.SetRepository;
import ru.yahoondex.archhelper.crawler.repositories.dao.GroupSet;
import ru.yahoondex.archhelper.crawler.repositories.dao.NameSet;
import ru.yahoondex.archhelper.crawler.services.helper.ArxivArticleHelper;
import ru.yahoondex.archhelper.crawler.services.helper.ArxivSetHelper;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ArxivService {
    private final ArxivClient client;
    private final SetRepository setRepository;
    private final GroupSetRepository groupSetRepository;
    private final KafkaTemplate<String, ArticleDto> articleTemplate;
    private final KafkaTemplate<String, NameSetDto> setTemplate;

    @Autowired
    public ArxivService(@Value("${arxiv.url}") String arxivUrl,
                        SetRepository setRepository,
                        GroupSetRepository groupSetRepository,
                        KafkaTemplate<String, ArticleDto> articleTemplate,
                        KafkaTemplate<String, NameSetDto> setTemplate) {
        client = new ArxivClient(arxivUrl);
        this.setRepository = setRepository;
        this.groupSetRepository = groupSetRepository;
        this.articleTemplate = articleTemplate;
        this.setTemplate = setTemplate;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * MON")
    public void updateSets() {
        client.getAllSets().subscribe(result -> {
            if (result == null) {
                log.error("Empty response from ArXiv set endpoint");
                return;
            }
            final Map<String, String> currentSets = setRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(NameSet::getId, NameSet::getName));
            final Map<String, String> futureSets = ArxivSetHelper.getNameSets(result)
                    .stream()
                    .collect(Collectors.toMap(NameSet::getId, NameSet::getName));
            futureSets.forEach((id, name) -> {
                if (currentSets.containsKey(id)) {
                    if (currentSets.get(id).equals(name))
                        return;
                    setRepository.save(new NameSet(id, name));
                    setTemplate.sendDefault(new NameSetDto(id, name, Operation.UPDATE));
                } else {
                    setRepository.save(new NameSet(id, name));
                    setTemplate.sendDefault(new NameSetDto(id, name, Operation.CREATE));
                }
            });
            currentSets.keySet().forEach(id -> {
                if (!futureSets.containsKey(id)) {
                    setRepository.deleteById(id);
                    setTemplate.sendDefault(new NameSetDto(id, null, Operation.DELETE));
                }
            });
        }, throwable -> {
            log.error("Error during set update query", throwable);
        });
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateArticlesDaily() {
        final LocalDate yesterday = LocalDate.now().minusDays(1);
        groupSetRepository.findAll().stream().map(GroupSet::getSetId).distinct().forEach(setId -> {
            log.info("Started loading articles for group {}", setId);
            client.getArticlesForPeriod(setId, yesterday, yesterday).subscribe(result -> {
                if (result == null) {
                    log.error("Empty response from ArXiv articles endpoint");
                    return;
                }
                ArxivArticleHelper.getArticles(result).forEach(articleDto -> {
                    articleTemplate.sendDefault(articleDto);
                });
            }, throwable -> {
                log.error("Error during daily articles fetch", throwable);
            });
        });

    }

}
